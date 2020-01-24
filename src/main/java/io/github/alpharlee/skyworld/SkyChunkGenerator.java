package io.github.alpharlee.skyworld;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.Random;

public class SkyChunkGenerator extends ChunkGenerator {
	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
		ChunkData chunk = createChunkData(world);
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
		generator.setScale(0.4D);

		// Double for loop to get all 256 blocks of surface
		int CHUNK_SIZE = 16;
		double variation = 100;
		double base = 50;
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int z = 0; z < CHUNK_SIZE; z++) {
				int currentHeight = (int) ((generator.noise(chunkX * CHUNK_SIZE + x, chunkZ * CHUNK_SIZE + z, 0.5, 0.5, true) + 1) * variation + base);

				populateColumnBlocks(chunk, x, currentHeight, z);
			}
		}

		return chunk;
	}

	private void populateColumnBlocks(ChunkData chunk, int x, int y, int z) {
		chunk.setBlock(x, y--, z, Material.GRASS_BLOCK);

		int dirtHeight = 2;
		for (int i = 0; i < dirtHeight; i++) {
			chunk.setBlock(x, y--, z, Material.DIRT);
		}

		int bottomAirHeight = 16;
		while (y > bottomAirHeight) {
			chunk.setBlock(x, y--, z, Material.STONE);
		}

		while (y  > 0) {
			chunk.setBlock(x, y--, z, Material.AIR);
		}
	}
}
