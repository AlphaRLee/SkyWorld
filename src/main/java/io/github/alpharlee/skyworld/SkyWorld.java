package io.github.alpharlee.skyworld;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkyWorld extends JavaPlugin {

	@Override
	public void onEnable() {
		// Plugin startup logic

	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		// TODO Add id as parameter (make customizable terrain settings)
		return new SkyChunkGenerator();
	}
}
