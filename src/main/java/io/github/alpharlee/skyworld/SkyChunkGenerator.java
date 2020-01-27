package io.github.alpharlee.skyworld;

import io.github.alpharlee.skyworld.populator.TreePopulator;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.*;

public class SkyChunkGenerator extends ChunkGenerator {
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

	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomeGrid) {
		ChunkData chunk = createChunkData(world);
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
//				if (isVoidBiome(getAverageBiome(biomeGrid, x, z, 5))) {
//					continue;
//				}

				int currentHeight = (int) (generator.noise(chunkX * CHUNK_SIZE + x, chunkZ * CHUNK_SIZE + z, TEST_VAL.get("cf"), TEST_VAL.get("ca"), true) * variation + base);
				double floorRand = floorGenerator.noise(chunkX * CHUNK_SIZE + x, chunkZ * CHUNK_SIZE + z, TEST_VAL.get("ff"), TEST_VAL.get("fa"), true);
//				int floorHeight = (int) (sigmoid(8D * (floorRand + 0.2)) * floorVariationMax + floorBase);
				int floorHeight = (int) (floorRand * floorVariationMax + floorBase);

				populateColumnBlocks(chunk, x, currentHeight, z, floorHeight);
			}
		}
		
		return chunk;
	}

	private Biome getAverageBiome(BiomeGrid biomeGrid, int x, int z, int sampleLength) {
		Map<Biome, Integer> biomeCounts = new HashMap<>(2);

		sampleLength = sampleLength % 2 == 1 ? sampleLength : sampleLength + 1; // Force sampleLength to be first odd number >= sampleLength
		for (int i = x - sampleLength / 2; i <= x + sampleLength / 2; i++) {
			for (int j = z - sampleLength / 2; j <= z + sampleLength / 2; j++) {
				Biome biome = biomeGrid.getBiome(i, 64, j);
				if (biomeCounts.containsKey(biome)) {
					biomeCounts.put(biome, biomeCounts.get(biome) + 1);
				} else {
					biomeCounts.put(biome, 1);
				}
			}
		}

		Biome maxBiome = null;
		int maxCount = 99999;
		for (Map.Entry<Biome, Integer> biomeCount : biomeCounts.entrySet()) {
			if (biomeCount.getValue() < maxCount) {
				maxBiome = biomeCount.getKey();
			}
		}

		return maxBiome;
	}

	private boolean isVoidBiome(Biome biome) {
		HashSet<Biome> voidBiomes = new HashSet<Biome>(Arrays.asList(
				Biome.OCEAN,
				Biome.RIVER,
				Biome.FROZEN_OCEAN,
				Biome.FROZEN_RIVER,
				Biome.WARM_OCEAN,
				Biome.LUKEWARM_OCEAN,
				Biome.COLD_OCEAN,
				Biome.DEEP_WARM_OCEAN,
				Biome.DEEP_LUKEWARM_OCEAN,
				Biome.DEEP_COLD_OCEAN,
				Biome.DEEP_FROZEN_OCEAN
		));

		return voidBiomes.contains(biome);
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

	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		return Arrays.asList((BlockPopulator) new TreePopulator());
	}
}
