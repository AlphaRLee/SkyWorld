package io.github.alpharlee.skyworld;

import org.bukkit.Chunk;
import org.bukkit.block.Biome;

import java.util.HashMap;
import java.util.Map;

public class BiomeCalculator {
	/**
	 * Iterates over the 256 surface blocks in this chunk to determine the most frequent biome that appears
	 * @param chunk
	 * @return The most frequent biome that appears
	 */
	public static Biome getAverageBiome(Chunk chunk) {
		Map<Biome, Integer> biomeCounts = new HashMap<>(2);

		for (int x = 0; x < SkyChunkGenerator.CHUNK_SIZE; x++) {
			for (int z = 0; z < SkyChunkGenerator.CHUNK_SIZE; z++) {
				Biome biome = chunk.getBlock(x, 64, z).getBiome();
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
}
