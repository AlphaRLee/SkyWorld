package io.github.alpharlee.skyworld;

import io.github.alpharlee.skyworld.decoration.DecorationData;
import io.github.alpharlee.skyworld.decoration.DecorationSpawnedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class EventListener implements Listener {
	private final SkyWorldConfig skyWorldConfig;

	public EventListener(SkyWorldConfig skyWorldConfig) {
		this.skyWorldConfig = skyWorldConfig;
	}

	@EventHandler
	public void onChunkPopulate(ChunkLoadEvent event) {
		if (!event.isNewChunk()) {
			return;
		}

		Chunk chunk = event.getChunk();

		// FIXME delete unused
//		Block dataBlock = chunk.getBlock(0, SkyWorldDecoration.DATA_Y, 0);
//		if (dataBlock.getType() != SkyWorldDecoration.DATA_BLOCK) {
//			return;
//		}
//
//		Chest chest = (Chest) dataBlock.getState();
//		Inventory inventory = chest.getInventory();
//		if (inventory.getItem(0) == null || inventory.getItem(0).getType() != SkyWorldDecoration.DATA_ITEM) {
//			return;
//		}
//
//		List<String> itemLore = inventory.getItem(0).getItemMeta().getLore();
//		if (itemLore.isEmpty()) {
//			return;
//		}
//
//		Yaml yaml = new Yaml();
//		LinkedHashMap<String, Object> yamlLoad = yaml.load(String.join("\n", itemLore));
//		DecorationData decorationData = new DecorationData(String.valueOf(yamlLoad.get("schematicName")),
//				Integer.valueOf(yamlLoad.get("x").toString()),
//				Integer.valueOf(yamlLoad.get("y").toString()),
//				Integer.valueOf(yamlLoad.get("z").toString()));
//
//		inventory.clear();
//		dataBlock.setType(Material.AIR); // Destroy the data block

		// FIXME delete attempt 2 ---------------------------------
//		WorldRef worldRef = WorldRef.of(event.getWorld());
//		DecorationChunk decorationChunk = new DecorationChunk(worldRef, chunk.getX(), chunk.getZ());
//		Map<DecorationChunk, List<DecorationData>> decorationDataMap = skyWorldConfig.getDecorationTargets().get(worldRef);
//
//		List<DecorationData> decorationDataList = decorationDataMap.get(decorationChunk);
//		if (decorationDataList == null) {
//			return;
//		}
//
//		for (DecorationData decorationData : decorationDataList) {
//			int x = decorationData.x, y = decorationData.y, z = decorationData.z;
//			SkyWorld.getInstance().getDecorationManager().pasteSchematic(decorationData.schematicName, event.getWorld(), x, y, z);
//
//			System.out.println("Set redstone lamp at " + decorationData.x + " " + decorationData.y + " " + decorationData.z);
//		}

	}

	@EventHandler
	public void onDecorationSpawn(DecorationSpawnedEvent event) {
		Bukkit.getLogger().info("!!! Event read at " + event.getDecorationData().x + " " + event.getDecorationData().y + " " + event.getDecorationData().z);
		World world = Bukkit.getWorld(event.getWorldRef().getName());
		DecorationData data = event.getDecorationData();
		SkyWorld.getInstance().getDecorationManager().pasteSchematic(data.schematicName, world, data.x, data.y, data.z);
	}
}
