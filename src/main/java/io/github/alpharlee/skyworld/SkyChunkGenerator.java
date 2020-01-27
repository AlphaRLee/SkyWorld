package io.github.alpharlee.skyworld;

import nl.rutgerkok.worldgeneratorapi.BaseChunkGenerator;
import nl.rutgerkok.worldgeneratorapi.WorldRef;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.LinkedHashMap;
import java.util.Random;

/**
 * SkyChunkGenerator depending on WorldGeneratorApi
 */
public class SkyChunkGenerator implements BaseChunkGenerator {
	public static final int CHUNK_SIZE = 16;

	// TODO Delete these debuggers
	public static LinkedHashMap<String, Double> TEST_VAL = new LinkedHashMap<>();
	static {
		TEST_VAL.put("co", 8D);
		TEST_VAL.put("cs", 0.02);

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
	}

	private final World world;

	public SkyChunkGenerator(World world) {
		this.world = world;
	}

	/**
	 * Sets the basic blocks (air, stone and water usually) in the chunk. No
	 * decorations are applied yet.
	 *
	 * @param chunk The chunk.
	 */
	@Override
	public void setBlocksInChunk(GeneratingChunk chunk) {
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), (int) Math.floor(TEST_VAL.get("co")));
		SimplexOctaveGenerator floorGenerator = new SimplexOctaveGenerator(new Random(world.getSeed() >> 1), (int) Math.floor(TEST_VAL.get("fo")));
		generator.setScale(TEST_VAL.get("cs"));
		floorGenerator.setScale(TEST_VAL.get("fs"));

		double variation = TEST_VAL.get("var");
		double base = TEST_VAL.get("base");

		double floorVariationMax = TEST_VAL.get("fvar");
		double floorBase = TEST_VAL.get("fbase");

		// Double for loop to get all 256 blocks of surface
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int z = 0; z < CHUNK_SIZE; z++) {

				int chunkX = chunk.getChunkX();
				int chunkZ = chunk.getChunkZ();

				int currentHeight = (int) (generator.noise(chunkX * CHUNK_SIZE + x, chunkZ * CHUNK_SIZE + z, TEST_VAL.get("cf"), TEST_VAL.get("ca"), true) * variation + base);
				double floorRand = floorGenerator.noise(chunkX * CHUNK_SIZE + x, chunkZ * CHUNK_SIZE + z, TEST_VAL.get("ff"), TEST_VAL.get("fa"), true);
				int floorHeight = (int) (floorRand * floorVariationMax + floorBase);

				populateColumnBlocks(chunk.getBlocksForChunk(), x, currentHeight, z, floorHeight);
			}
		}
	}

	private void populateColumnBlocks(ChunkData chunk, int x, int y, int z, int floorHeight) {
		Material[] surfaceMaterials = {Material.GRASS_BLOCK, Material.DIRT, Material.DIRT};

		int i = 0;
		while (y > floorHeight && y > 0) {
			if (i < surfaceMaterials.length) {
				chunk.setBlock(x, y, z, surfaceMaterials[i++]);
			} else {
				chunk.setBlock(x, y, z, Material.STONE);
			}

			y--;
		}
	}
}
