package io.github.alpharlee.skyworld.populator;

import io.github.alpharlee.skyworld.SkyChunkGenerator;
import io.github.alpharlee.skyworld.biomedata.TreeBiomeData;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TreePopulator extends BlockPopulator {
	HashMap<Biome, TreeBiomeData> treeBiomeDataLookup;

	public TreePopulator() {
		treeBiomeDataLookup = new HashMap<>();
		addTreeEntry(Biome.PLAINS, 1, 5, 0.2, TreeType.TREE, TreeType.BIG_TREE, TreeType.BIRCH);
		addTreeEntry(Biome.MOUNTAINS, 1, 4, 0.2, TreeType.REDWOOD, TreeType.TALL_REDWOOD, TreeType.TREE, TreeType.BIG_TREE);

		TreeBiomeData forestData = new TreeBiomeData(4, 9, TreeType.TREE, TreeType.BIG_TREE, TreeType.BIRCH);
		addTreeEntry(Biome.FOREST, forestData);
		addTreeEntry(Biome.WOODED_HILLS, forestData);
		addTreeEntry(Biome.WOODED_MOUNTAINS, forestData);

		TreeBiomeData taigaData = new TreeBiomeData(3, 6, TreeType.REDWOOD, TreeType.TALL_REDWOOD);
		addTreeEntry(Biome.TAIGA, taigaData);
		addTreeEntry(Biome.TAIGA_HILLS, taigaData);
		addTreeEntry(Biome.TAIGA_MOUNTAINS, taigaData);
		addTreeEntry(Biome.SNOWY_TAIGA_HILLS, taigaData);
		addTreeEntry(Biome.SNOWY_TAIGA_MOUNTAINS, taigaData);

		TreeBiomeData swampData = new TreeBiomeData(1, 6, 0.7, TreeType.SWAMP, TreeType.TREE);
		addTreeEntry(Biome.SWAMP, swampData);
		addTreeEntry(Biome.SWAMP_HILLS, swampData);

		TreeBiomeData mushroomData = new TreeBiomeData(1, 3, 0.7, TreeType.RED_MUSHROOM, TreeType.BROWN_MUSHROOM);
		addTreeEntry(Biome.MUSHROOM_FIELDS, mushroomData);
		addTreeEntry(Biome.MUSHROOM_FIELD_SHORE, mushroomData);

		TreeBiomeData jungleData = new TreeBiomeData(3, 9, TreeType.TREE, TreeType.JUNGLE, TreeType.JUNGLE_BUSH, TreeType.SMALL_JUNGLE, TreeType.COCOA_TREE);
		addTreeEntry(Biome.JUNGLE, jungleData);
		addTreeEntry(Biome.JUNGLE_HILLS, jungleData);
		addTreeEntry(Biome.MODIFIED_JUNGLE, jungleData);

		addTreeEntry(Biome.JUNGLE_EDGE, 1, 6, TreeType.TREE, TreeType.JUNGLE_BUSH, TreeType.SMALL_JUNGLE, TreeType.COCOA_TREE);
		addTreeEntry(Biome.MODIFIED_JUNGLE_EDGE, 1, 6, TreeType.TREE, TreeType.JUNGLE_BUSH, TreeType.SMALL_JUNGLE, TreeType.COCOA_TREE);

		TreeBiomeData birchData = new TreeBiomeData(2, 6, TreeType.BIRCH);
		addTreeEntry(Biome.BIRCH_FOREST, birchData);
		addTreeEntry(Biome.BIRCH_FOREST_HILLS, birchData);

		TreeBiomeData tallBirchData = new TreeBiomeData(2, 6, TreeType.BIRCH, TreeType.TALL_BIRCH);
		addTreeEntry(Biome.BIRCH_FOREST, tallBirchData);
		addTreeEntry(Biome.BIRCH_FOREST_HILLS, tallBirchData);

		TreeBiomeData darkForestData = new TreeBiomeData(1, 6, TreeType.TREE, TreeType.DARK_OAK, TreeType.BROWN_MUSHROOM, TreeType.TREE.RED_MUSHROOM);
		addTreeEntry(Biome.DARK_FOREST, darkForestData);
		addTreeEntry(Biome.DARK_FOREST_HILLS, darkForestData);

		TreeBiomeData giantTaigaData = new TreeBiomeData(1, 6, TreeType.REDWOOD, TreeType.TALL_REDWOOD, TreeType.MEGA_REDWOOD);
		addTreeEntry(Biome.GIANT_TREE_TAIGA, giantTaigaData);
		addTreeEntry(Biome.GIANT_TREE_TAIGA_HILLS, giantTaigaData);
		addTreeEntry(Biome.GIANT_SPRUCE_TAIGA, giantTaigaData);
		addTreeEntry(Biome.GIANT_SPRUCE_TAIGA_HILLS, giantTaigaData);

		TreeBiomeData savannaData = new TreeBiomeData(1, 3, 0.4, TreeType.ACACIA);
		addTreeEntry(Biome.SAVANNA, savannaData);
		addTreeEntry(Biome.SAVANNA_PLATEAU, savannaData);
		addTreeEntry(Biome.SHATTERED_SAVANNA, savannaData);
		addTreeEntry(Biome.SHATTERED_SAVANNA_PLATEAU, savannaData);

		addTreeEntry(Biome.FLOWER_FOREST, 1, 5, 0.8, TreeType.TREE, TreeType.BIRCH);

		TreeBiomeData bambooJungleData = new TreeBiomeData(3, 6, 0.6, TreeType.JUNGLE, TreeType.SMALL_JUNGLE);
		addTreeEntry(Biome.BAMBOO_JUNGLE, bambooJungleData);
		addTreeEntry(Biome.BAMBOO_JUNGLE_HILLS, bambooJungleData);
	}


	private void addTreeEntry(Biome biome, int minDensity, int maxDensity, TreeType... treeTypes) {
		treeBiomeDataLookup.put(biome, new TreeBiomeData(minDensity, maxDensity, treeTypes));
	}

	private void addTreeEntry(Biome biome, int minDensity, int maxDensity, double spawnProbability, TreeType... treeTypes) {
		treeBiomeDataLookup.put(biome, new TreeBiomeData(minDensity, maxDensity, spawnProbability, treeTypes));
	}

	private void addTreeEntry(Biome biome, TreeBiomeData treeBiomeData) {
		treeBiomeDataLookup.put(biome, treeBiomeData);
	}

	@Override
	public void populate(World world, Random random, Chunk chunk) {
		if (!random.nextBoolean()) {
			return;
		}

		int maxTreeCount = 5;
		int treeAmount = random.nextInt(maxTreeCount) + 1;

		for (int i = 0; i < treeAmount; i++) {
			placeTree(world, random, chunk);
		}
	}

	private void placeTree(World world, Random random, Chunk chunk) {
		int x = random.nextInt(SkyChunkGenerator.CHUNK_SIZE);
		int z = random.nextInt(SkyChunkGenerator.CHUNK_SIZE);
		int y = world.getMaxHeight() - 1;
		boolean isValidPosition = false;
		while (!isValidPosition && y > 0) {
			isValidPosition = chunk.getBlock(x, y - 1, z).getType() != Material.GRASS_BLOCK;
			y--;
		}

		if (!isValidPosition) {
			return;
		}

		world.generateTree(chunk.getBlock(x, y, z).getLocation(), TreeType.TREE);
	}
}
