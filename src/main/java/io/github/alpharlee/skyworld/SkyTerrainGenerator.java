package io.github.alpharlee.skyworld;

import nl.rutgerkok.worldgeneratorapi.BaseTerrainGenerator;
import nl.rutgerkok.worldgeneratorapi.WorldRef;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.util.noise.OctaveGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.Random;

/**
 * SkyChunkGenerator depending on WorldGeneratorApi
 */
public class SkyTerrainGenerator implements BaseTerrainGenerator {
	public static final int CHUNK_SIZE = 16;

	private final WorldRef worldRef;
	private final World world;
	private final SkyWorldConfig skyWorldConfig;

	public SkyTerrainGenerator(WorldRef worldRef, World world, SkyWorldConfig skyWorldConfig) {
		this.worldRef = worldRef;
		this.world = world;
		this.skyWorldConfig = skyWorldConfig;
	}

	public void setBlocksInChunk(GeneratingChunk chunk) {
		int landOctaves = (int) skyWorldConfig.getLandOctaves().get(worldRef);
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), landOctaves);
		SimplexOctaveGenerator maxHeightGenerator = new SimplexOctaveGenerator(new Random(world.getSeed() >> 1), 5);

		int chunkX = chunk.getChunkX() * CHUNK_SIZE;
		int chunkZ = chunk.getChunkZ() * CHUNK_SIZE;

		// TODO This is terrible on server performance, find a better solution
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int z = 0; z < CHUNK_SIZE; z++) {
				populateColumnBlocks(chunk.getBlocksForChunk(), chunkX + x, chunkZ + z, generator, maxHeightGenerator);
			}
		}
	}

	private void populateColumnBlocks(ChunkData chunkData, int x, int z, OctaveGenerator generator, OctaveGenerator maxHeightGenerator) {
		Material[] caveSurfaceMaterials = {Material.GRASS_BLOCK};

		double landScale = (double) skyWorldConfig.getLandScale().get(worldRef);
		double landYScale = (double) skyWorldConfig.getLandYScale().get(worldRef);
		generator.setScale(landScale);
		generator.setYScale(landYScale);

		maxHeightGenerator.setScale(0.01);

		int y = (int) (maxHeightGenerator.noise(x, z, 1.2, 0.5, true) * 40.0 + 160);
		if (y >= world.getMaxHeight()) {
			y = world.getMaxHeight() - 1;
		}

		int floorHeight = (int) (maxHeightGenerator.noise(x, z, 1.2, 0.5, true) * 8.0 + 8);
		if (floorHeight <= 0) {
			floorHeight = 0;
		}

		double landFrequency = (double) skyWorldConfig.getLandFrequency().get(worldRef);
		double landAmplitude = (double) skyWorldConfig.getLandAmplitude().get(worldRef);
		double landThreshold = (double) skyWorldConfig.getLandThreshold().get(worldRef);

		int i = 0;
		boolean isSolid;
		boolean isSolidAbove = false;
		while (y > floorHeight) {

			isSolid = generator.noise(x, y,  z, landFrequency, landAmplitude, true) >= landThreshold;
			if (isSolid) {
				if (y <= 50 && i < caveSurfaceMaterials.length && !isSolidAbove) {
					chunkData.setBlock(x, y, z, caveSurfaceMaterials[i++]);
				} else {
					chunkData.setBlock(x, y, z, Material.STONE);
				}
			} else {
				i = 0;
			}

			isSolidAbove = isSolid;
			y--;
		}
	}

	/**
	 * Predicts the height for the given x and z coordinate, which can be anywhere
	 * in the world. For a superflat world, this is just a constant value, but for
	 * other world types the calculation will be more complex.
	 *
	 * @param x    X in the world.
	 * @param z    Y in the world.
	 * @param type The type of heightmap.
	 * @return The height, for example 63.
	 */
	@Override
	public int getHeight(int x, int z, HeightType type) {
		int landOctaves = (int) skyWorldConfig.getLandOctaves().get(worldRef);
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), landOctaves);
		double landFrequency = (double) skyWorldConfig.getLandFrequency().get(worldRef);
		double landAmplitude = (double) skyWorldConfig.getLandAmplitude().get(worldRef);
		double landThreshold = (double) skyWorldConfig.getLandThreshold().get(worldRef);

		int y = (int) Math.floor(Math.random() * 255);

		int i = 0;
		boolean isSolid;
		boolean isSolidAbove = false;
		while (y > 0) {
			isSolid = generator.noise(x, y,  z, landFrequency, landAmplitude, true) >= landThreshold;
			if (isSolid) {
				return y;
			}
			y--;
		}

		return -1;
	}
}
