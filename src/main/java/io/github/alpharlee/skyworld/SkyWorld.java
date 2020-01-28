package io.github.alpharlee.skyworld;

import io.github.alpharlee.skyworld.decorator.AirshipDecoration;
import nl.rutgerkok.worldgeneratorapi.WorldGeneratorApi;
import nl.rutgerkok.worldgeneratorapi.WorldRef;
import nl.rutgerkok.worldgeneratorapi.decoration.BaseDecorationType;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedHashMap;
import java.util.Map;

public final class SkyWorld extends JavaPlugin {

	private SkyWorldConfig skyWorldConfig;
	private WorldGeneratorApi worldGeneratorApi;

	@Override
	public void onEnable() {
		// Plugin startup logic
		int worldGeneratorApiVersionMajor = 0;
		int worldGeneratorApiVersionMinor = 4;
		worldGeneratorApi = WorldGeneratorApi.getInstance(this, worldGeneratorApiVersionMajor, worldGeneratorApiVersionMinor);

		skyWorldConfig = new SkyWorldConfig(this, worldGeneratorApi.getPropertyRegistry());

		// TODO delete this debugger
//		LinkedHashMap<String, Double> TEST_VAL = SkyTerrainGenerator.TEST_VAL;
//		for (String key : TEST_VAL.keySet()) {
//			TEST_VAL.put(key, config.getDouble(key));
//		}
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
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
			generator.getWorldDecorator().withCustomDecoration(DecorationType.SURFACE_STRUCTURES, new AirshipDecoration(generator.getWorld()));

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

	// FIXME delete
	private void setConfigCmd(CommandSender sender, String[] args) {
		LinkedHashMap<String, Double> TEST_VAL = SkyTerrainGenerator.TEST_VAL;

		if (args.length < 2) {
			sender.sendMessage("---------");
			for (Map.Entry<String, Double> vals : TEST_VAL.entrySet()) {
				sender.sendMessage(vals.getKey() + ": " + vals.getValue());
			}
			sender.sendMessage("---------\nRemember to save!");
			return;
		}

		String arg0 = args[1].toLowerCase();
		if (!TEST_VAL.containsKey(arg0)) {
			sender.sendMessage("Error, can't recognize " + arg0);
			return;
		}

		if (args.length < 3) {
			sender.sendMessage(arg0 + ": " + TEST_VAL.get(arg0));
			return;
		}

		Double arg1 = Double.valueOf(args[2].toLowerCase());
		TEST_VAL.put(arg0, arg1);
		config.set(arg0, arg1);
		sender.sendMessage("Changed " + arg0 + " to " + arg1);
	}
}
