package io.github.alpharlee.skyworld;

import io.github.alpharlee.skyworld.decoration.AirshipDecoration;
import nl.rutgerkok.worldgeneratorapi.WorldGeneratorApi;
import nl.rutgerkok.worldgeneratorapi.WorldRef;
import nl.rutgerkok.worldgeneratorapi.decoration.BaseDecorationType;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class SkyWorld extends JavaPlugin {

	private static SkyWorld instance;

	private SkyWorldConfig skyWorldConfig;
	private WorldGeneratorApi worldGeneratorApi;

	@Override
	public void onEnable() {
		instance = this;

		setupFileStructure();

		int apiVersionMajor = 0;
		int apiVersionMinor = 4;
		worldGeneratorApi = WorldGeneratorApi.getInstance(this, apiVersionMajor, apiVersionMinor);

		skyWorldConfig = new SkyWorldConfig(this, worldGeneratorApi.getPropertyRegistry());

		getServer().getPluginManager().registerEvents(new EventListener(), this);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	public static SkyWorld getInstance() {
		return instance;
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		// TODO Add id as parameter (make customizable terrain settings)
//		return new SkyChunkGenerator();

		WorldRef worldRef = WorldRef.ofName(worldName);
		return worldGeneratorApi.createCustomGenerator(worldRef, generator -> {
			skyWorldConfig.readConfig(generator.getWorldRef(), getConfig());

			generator.setBaseTerrainGenerator(new SkyTerrainGenerator(generator.getWorldRef(), generator.getWorld(), skyWorldConfig));
			generator.getWorldDecorator().withoutDefaultBaseDecorations(BaseDecorationType.CARVING_LIQUID);
			generator.getWorldDecorator().withoutDefaultBaseDecorations(BaseDecorationType.BEDROCK);
			generator.getWorldDecorator().withCustomDecoration(DecorationType.SURFACE_STRUCTURES, new AirshipDecoration(generator.getWorld(), skyWorldConfig));

			// Update the config and save it with valid values
			skyWorldConfig.writeConfig(worldRef, getConfig());
			saveConfig();

			getLogger().info("Enabling SkyWorld generator for world " + worldName);
		});
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String cmdStr = command.getName();
		if (!cmdStr.equalsIgnoreCase("skyworld")) {
			return false;
		}

		// FIXME delete this
		if (args.length >= 1 && args[0].equalsIgnoreCase("save")) {
			saveConfig();
			sender.sendMessage("Config saved!");
			return true;
		}

//		setConfigCmd(sender, args);
		return true;
	}

	private void setupFileStructure() {
		File schematicsDirectory = new File(getDataFolder() + File.separator + "schematics" + File.separator);
		if (!schematicsDirectory.exists()) {
			schematicsDirectory.mkdirs();
		}
	}
}
