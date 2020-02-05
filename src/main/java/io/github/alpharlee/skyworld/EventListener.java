package io.github.alpharlee.skyworld;

import org.bukkit.event.Listener;

public class EventListener implements Listener {
	private final SkyWorldConfig skyWorldConfig;

	public EventListener(SkyWorldConfig skyWorldConfig) {
		this.skyWorldConfig = skyWorldConfig;
	}

	// FIXME delete unused
//	public void onDecorationSpawn(DecorationSpawnedEvent event) {
//		Bukkit.getLogger().info("!!! Event read at " + event.getDecorationData().x + " " + event.getDecorationData().y + " " + event.getDecorationData().z);
//		World world = Bukkit.getWorld(event.getWorldRef().getName());
//		DecorationData data = event.getDecorationData();
//		SkyWorld.getInstance().getDecorationManager().pasteSchematic(data.schematicName, world, data.x, data.y, data.z);
//	}
}
