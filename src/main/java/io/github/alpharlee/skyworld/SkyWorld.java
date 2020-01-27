package io.github.alpharlee.skyworld;

import nl.rutgerkok.worldgeneratorapi.WorldGeneratorApi;
import nl.rutgerkok.worldgeneratorapi.WorldRef;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedHashMap;
import java.util.Map;

public final class SkyWorld extends JavaPlugin {

	FileConfiguration config;

	@Override
	public void onEnable() {
		// Plugin startup logic

		config = getConfig();

		// TODO delete this debugger
		LinkedHashMap<String, Double> TEST_VAL = SkyChunkGenerator.TEST_VAL;
		for (String key : TEST_VAL.keySet()) {
			TEST_VAL.put(key, config.getDouble(key));
		}
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		// TODO Add id as parameter (make customizable terrain settings)
//		return new SkyChunkGenerator();

		int worldGeneratorApiVersionMajor = 0;
		int worldGeneratorApiVersionMinor = 4;
		return WorldGeneratorApi.getInstance(this, worldGeneratorApiVersionMajor, worldGeneratorApiVersionMinor)
				.createCustomGenerator(WorldRef.ofName(worldName), generator -> {
					generator.setBaseChunkGenerator(new SkyChunkGenerator(generator.getWorld()));
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

		TEST_setVals(sender, args);
		return true;
	}

	// FIXME delete
	private void TEST_setVals(CommandSender sender, String[] args) {
		LinkedHashMap<String, Double> TEST_VAL = SkyChunkGenerator.TEST_VAL;

		if (args.length < 1) {
			sender.sendMessage("---------");
			for (Map.Entry<String, Double> vals : TEST_VAL.entrySet()) {
				sender.sendMessage(vals.getKey() + ": " + vals.getValue());
			}
			sender.sendMessage("---------\nRemember to save!");
			return;
		}

		String arg0 = args[0].toLowerCase();
		if (!TEST_VAL.containsKey(arg0)) {
			sender.sendMessage("Error, can't recognize " + arg0);
			return;
		}

		if (args.length < 2) {
			sender.sendMessage(arg0 + ": " + TEST_VAL.get(arg0));
			return;
		}

		Double arg1 = Double.valueOf(args[1].toLowerCase());
		TEST_VAL.put(arg0, arg1);
		config.set(arg0, arg1);
		sender.sendMessage("Changed " + arg0 + " to " + arg1);
	}
}
