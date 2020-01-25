package io.github.alpharlee.skyworld.biomedata;

import org.bukkit.TreeType;

import java.util.Arrays;
import java.util.List;

public class TreeBiomeData {
	private int minDensity;
	private int maxDensity;
	private double spawnProbability;
	private List<TreeType> treeTypes;

	public TreeBiomeData(int minDensity, int maxDensity, TreeType... treeTypes) {
		this(minDensity, maxDensity, 0.98, treeTypes);
	}

	public TreeBiomeData(int minDensity, int maxDensity, double spawnProbability, TreeType... treeTypes) {
		this.minDensity = minDensity;
		this.maxDensity = maxDensity;
		this.spawnProbability = spawnProbability;
		this.treeTypes = Arrays.asList(treeTypes);
	}

	public int getMinDensity() {
		return minDensity;
	}

	public void setMinDensity(int minDensity) {
		this.minDensity = minDensity;
	}

	public int getMaxDensity() {
		return maxDensity;
	}

	public void setMaxDensity(int maxDensity) {
		this.maxDensity = maxDensity;
	}

	public List<TreeType> getTreeTypes() {
		return treeTypes;
	}

	public void setTreeTypes(List<TreeType> treeTypes) {
		this.treeTypes = treeTypes;
	}

	public double getSpawnProbability() {
		return spawnProbability;
	}

	public void setSpawnProbability(double spawnProbability) {
		this.spawnProbability = spawnProbability;
	}
}
