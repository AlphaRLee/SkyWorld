package io.github.alpharlee.skyworld;

import io.github.alpharlee.skyworld.decoration.DecorationData;
import io.github.alpharlee.skyworld.decoration.SkyWorldDecoration;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.inventory.Inventory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.util.LinkedHashMap;
import java.util.List;

public class EventListener implements Listener {
	@EventHandler
	public void onChunkPopulate(ChunkLoadEvent event) {
		if (!event.isNewChunk()) {
			return;
		}

		Chunk chunk = event.getChunk();

		Block dataBlock = chunk.getBlock(0, SkyWorldDecoration.DATA_Y, 0);
		if (dataBlock.getType() != SkyWorldDecoration.DATA_BLOCK) {
			return;
		}

		Chest chest = (Chest) dataBlock.getState();
		Inventory inventory = chest.getInventory();
		if (inventory.getItem(0) == null || inventory.getItem(0).getType() != SkyWorldDecoration.DATA_ITEM) {
			return;
		}

		List<String> itemLore = inventory.getItem(0).getItemMeta().getLore();
		if (itemLore.isEmpty()) {
			return;
		}

		Yaml yaml = new Yaml();
		LinkedHashMap<String, Object> yamlLoad = yaml.load(String.join("\n", itemLore));
		DecorationData decorationData = new DecorationData(String.valueOf(yamlLoad.get("name")),
				Integer.valueOf(yamlLoad.get("x").toString()),
				Integer.valueOf(yamlLoad.get("y").toString()),
				Integer.valueOf(yamlLoad.get("z").toString()));

		dataBlock.setType(Material.AIR); // Destroy the data block

		int x = decorationData.x, y = decorationData.y, z = decorationData.z;
//		event.getWorld().getBlockAt(decorationData.x, decorationData.y, decorationData.z).setType(Material.REDSTONE_LAMP);
		SkyWorld.getInstance().getDecorationManager().pasteSchematic(event.getWorld(), decorationData.name, x, y, z);

		System.out.println("Set redstone lamp at " + decorationData.x + " " + decorationData.y + " " + decorationData.z);
	}
}
