package io.github.alpharlee.skyworld;

import nl.rutgerkok.worldgeneratorapi.BaseChunkGenerator;
import nl.rutgerkok.worldgeneratorapi.BaseTerrainGenerator;
import nl.rutgerkok.worldgeneratorapi.WorldRef;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.util.noise.OctaveGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.LinkedHashMap;
import java.util.Random;

/**
 * SkyChunkGenerator depending on WorldGeneratorApi
 */
public class SkyTerrainGenerator implements BaseTerrainGenerator {
	public static final int CHUNK_SIZE = 16;

	// TODO Delete these debuggers
	public static LinkedHashMap<String, Double> TEST_VAL = new LinkedHashMap<>();
	static {
		TEST_VAL.put("co", 8D);
		TEST_VAL.put("cs", 0.02);
		TEST_VAL.put("csy", 0.02);

		TEST_VAL.put("fo", 5D);
		TEST_VAL.put("fs", 0.013);

		TEST_VAL.put("cf", 1.6);
		TEST_VAL.put("ca", 0.5);
		TEST_VAL.put("ff", 1.2);
		TEST_VAL.put("fa", 0.5);
		TEST_VAL.put("var", 30D);
		TEST_VAL.put("base", 30D);
		TEST_VAL.put("fvar", 45D);
		TEST_VAL.put("fbase", 20D);

		TEST_VAL.put("threshold", 0.6D);
	}

	private final World world;

	public SkyTerrainGenerator(World world) {
		this.world = world;
	}

	public void setBlocksInChunk(GeneratingChunk chunk) {
//		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), (int) Math.floor(TEST_VAL.get("co")));
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
		SimplexOctaveGenerator maxHeightGenerator = new SimplexOctaveGenerator(new Random(world.getSeed() >> 1), 5);

//		double variation = TEST_VAL.get("var");
//		double base = TEST_VAL.get("base");

		int minHeight = 8;
		int maxHeight = 100;

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
		Material[] surfaceMaterials = {Material.GRASS_BLOCK, Material.DIRT, Material.DIRT};
		Material[] caveSurfaceMaterials = {Material.GRASS_BLOCK};

//		generator.setScale(TEST_VAL.get("cs"));
		generator.setScale(0.004);
		generator.setYScale(0.004);

//		maxHeightGenerator.setScale(TEST_VAL.get("fs"));
		maxHeightGenerator.setScale(0.01);

//		int y = (int) (maxHeightGenerator.noise(x, z, TEST_VAL.get("ff"), TEST_VAL.get("fa"), true) * TEST_VAL.get("fvar") + TEST_VAL.get("fbase"));
		int y = (int) (maxHeightGenerator.noise(x, z, 1.2, 0.5, true) * 40.0 + 160);
		if (y >= world.getMaxHeight()) {
			y = world.getMaxHeight() - 1;
		}
//		int floorHeight = (int) (maxHeightGenerator.noise(x, z, TEST_VAL.get("ff"), TEST_VAL.get("fa"), true) * 8 + 8);
		int floorHeight = (int) (maxHeightGenerator.noise(x, z, 1.2, 0.5, true) * 8.0 + 8);
		if (floorHeight <= 0) {
			floorHeight = 0;
		}

		int i = 0;
		while (y > floorHeight) {
//			double threshold = TEST_VAL.get("threshold");
			double threshold = 0.4;
//			boolean isSolid = generator.noise(x, y,  z, TEST_VAL.get("cf"), TEST_VAL.get("ca"), true) >= threshold;
			boolean isSolid = generator.noise(x, y,  z, 1.6, 0.7, true) >= threshold;

			if (isSolid) {
				if (y <= 50 && i < caveSurfaceMaterials.length) {
					chunkData.setBlock(x, y, z, caveSurfaceMaterials[i++]);
				} else {
					chunkData.setBlock(x, y, z, Material.STONE);
				}
			} else {
				i = 0;
			}

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
		return 64; // TODO fixme
	}
}
