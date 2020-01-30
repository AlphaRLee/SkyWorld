package io.github.alpharlee.skyworld.decoration;

import io.github.alpharlee.skyworld.SkyTerrainGenerator;
import nl.rutgerkok.worldgeneratorapi.decoration.Decoration;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.List;

public abstract class SkyWorldDecoration implements Decoration{
	public static final int DATA_Y = 255;
	public static final Material DATA_BLOCK = Material.TRAPPED_CHEST;
	public static final Material DATA_ITEM = Material.POISONOUS_POTATO;

	public abstract String getDecorationName();

	protected void spawn(DecorationArea area, int x, int y, int z) {
		DecorationData data = new DecorationData(getDecorationName(), x, y, z);

		Yaml yaml = new Yaml();
		String serializedData = yaml.dumpAsMap(data);

		ItemStack dataItem = new ItemStack(DATA_ITEM);
		ItemMeta meta = dataItem.getItemMeta();
		List<String> loreLines = new ArrayList<>();
		for (String line : serializedData.split("\n")) {
			loreLines.add(line);
		}
		meta.setLore(loreLines);
		dataItem.setItemMeta(meta);

		int dataX = x / SkyTerrainGenerator.CHUNK_SIZE * SkyTerrainGenerator.CHUNK_SIZE;
		int dataZ = z / SkyTerrainGenerator.CHUNK_SIZE * SkyTerrainGenerator.CHUNK_SIZE;
		int dataY = DATA_Y;

		area.setBlock(dataX, dataY, dataZ, DATA_BLOCK);
		Chest chest = (Chest) area.getBlockState(dataX, dataY, dataZ);
		Inventory chestInventory = chest.getInventory();
		chestInventory.setItem(0, dataItem);

		area.setBlockState(dataX, dataY, dataZ, chest);

		System.out.println("Chest for " + x + " " + y+  " " + z + " at " + dataX + " " + dataY + " " + dataZ);
	}
}
