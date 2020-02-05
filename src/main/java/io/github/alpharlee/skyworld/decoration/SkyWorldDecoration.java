package io.github.alpharlee.skyworld.decoration;

import io.github.alpharlee.skyworld.SkyTerrainGenerator;
import io.github.alpharlee.skyworld.SkyWorldConfig;
import nl.rutgerkok.worldgeneratorapi.WorldRef;
import nl.rutgerkok.worldgeneratorapi.decoration.Decoration;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;
import nl.rutgerkok.worldgeneratorapi.property.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class SkyWorldDecoration implements Decoration{
	public static final int DATA_Y = 255;
	public static final Material DATA_BLOCK = Material.TRAPPED_CHEST;
	public static final Material DATA_ITEM = Material.POISONOUS_POTATO;

	public abstract String getSchematicName();

	private final WorldRef worldRef;
	private final SkyWorldConfig skyWorldConfig;

	public SkyWorldDecoration(WorldRef worldRef, SkyWorldConfig skyWorldConfig) {
		this.worldRef = worldRef;
		this.skyWorldConfig = skyWorldConfig;
	}

	protected void spawn(DecorationArea area, int x, int y, int z) {
		// FIXME delete attempt 1
//		DecorationData data = new DecorationData(getSchematicName(), x, y, z);
//
//		Yaml yaml = new Yaml();
//		String serializedData = yaml.dumpAsMap(data);
//
//		ItemStack dataItem = new ItemStack(DATA_ITEM);
//		ItemMeta meta = dataItem.getItemMeta();
//		List<String> loreLines = new ArrayList<>();
//		for (String line : serializedData.split("\n")) {
//			loreLines.add(line);
//		}
//		meta.setLore(loreLines);
//		dataItem.setItemMeta(meta);
//
//		int dataX = x / SkyTerrainGenerator.CHUNK_SIZE * SkyTerrainGenerator.CHUNK_SIZE;
//		int dataZ = z / SkyTerrainGenerator.CHUNK_SIZE * SkyTerrainGenerator.CHUNK_SIZE;
//		int dataY = DATA_Y;
//
//		area.setBlock(dataX, dataY, dataZ, DATA_BLOCK);
//		Chest chest = (Chest) area.getBlockState(dataX, dataY, dataZ);
//		Inventory chestInventory = chest.getInventory();
//		chestInventory.setItem(0, dataItem);
//
//		area.setBlockState(dataX, dataY, dataZ, chest);
//
//		System.out.println("Chest for " + x + " " + y+  " " + z + " at " + dataX + " " + dataY + " " + dataZ);

		// ------------------ FIXME delete attempt 2
//		DecorationChunk decorationChunk = new DecorationChunk(worldRef, x / SkyTerrainGenerator.CHUNK_SIZE, z / SkyTerrainGenerator.CHUNK_SIZE);
//		DecorationData data = new DecorationData(worldRef, getSchematicName(), x, y, z);
//		Map<DecorationChunk, List<DecorationData>> dataMap = skyWorldConfig.getDecorationTargets().get(worldRef);
//		if (dataMap.containsKey(decorationChunk)) {
//			dataMap.get(decorationChunk).add(data);
//		} else {
//			List<DecorationData> chunkDataList = new ArrayList<>();
//			chunkDataList.add(data);
//			dataMap.put(decorationChunk, chunkDataList);
//		}

		DecorationData data = new DecorationData(worldRef, getSchematicName(), x, y, z);
		DecorationSpawnedEvent event = new DecorationSpawnedEvent(worldRef, data);
		Bukkit.getPluginManager().callEvent(event);
		Bukkit.getLogger().info("!!! Decoration called event at " + x + " " + y + " " + z); // FIXME delete
	}
}
