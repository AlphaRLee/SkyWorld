package io.github.alpharlee.skyworld;

import io.github.alpharlee.skyworld.commandhandler.CommandHandler;
import io.github.alpharlee.skyworld.decoration.AirDecoration;
import io.github.alpharlee.skyworld.decoration.DecorationSettings;
import io.github.alpharlee.skyworld.decoration.DynamicDecoration;
import nl.rutgerkok.worldgeneratorapi.WorldGeneratorApi;
import nl.rutgerkok.worldgeneratorapi.WorldRef;
import nl.rutgerkok.worldgeneratorapi.decoration.BaseDecorationType;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationType;
import nl.rutgerkok.worldgeneratorapi.decoration.WorldDecorator;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class SkyWorld extends JavaPlugin {

	private static SkyWorld instance;

	private SkyWorldConfig skyWorldConfig;
	private WorldGeneratorApi worldGeneratorApi;

	private DecorationManager decorationManager;

	private CommandHandler commandHandler;

	@Override
	public void onEnable() {
		instance = this;

		setupFileStructure();

		ConfigurationSerialization.registerClass(DecorationSettings.class, "DecorationSettings");

		int apiVersionMajor = 0;
		int apiVersionMinor = 4;
		worldGeneratorApi = WorldGeneratorApi.getInstance(this, apiVersionMajor, apiVersionMinor);

		decorationManager = new DecorationManager(getConfig().getString("schematics", getDataFolder() + File.separator + "schematics"));
//		decorationManager.loadDecorationsFromConfig(getConfig());
		skyWorldConfig = new SkyWorldConfig(this, worldGeneratorApi.getPropertyRegistry());

		getServer().getPluginManager().registerEvents(new EventListener(skyWorldConfig), this);

		this.commandHandler = new CommandHandler();
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	public static SkyWorld getInstance() {
		return instance;
	}

	public DecorationManager getDecorationManager() {
		return decorationManager;
	}

	public SkyWorldConfig getSkyWorldConfig() {
		return skyWorldConfig;
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		// TODO Add id as parameter (make customizable terrain settings)

		WorldRef worldRef = WorldRef.ofName(worldName);
		return worldGeneratorApi.createCustomGenerator(worldRef, generator -> {
			skyWorldConfig.readConfig(generator.getWorldRef(), getConfig());

			generator.setBaseTerrainGenerator(new SkyTerrainGenerator(generator.getWorldRef(), generator.getWorld(), skyWorldConfig));
			generator.getWorldDecorator().withoutDefaultBaseDecorations(BaseDecorationType.CARVING_LIQUID);
			generator.getWorldDecorator().withoutDefaultBaseDecorations(BaseDecorationType.BEDROCK);

			loadDynamicDecorations(generator.getWorldDecorator(), generator.getWorld());

			// Update the config and save it with valid values
			skyWorldConfig.writeConfig(worldRef, getConfig());
			saveConfig();

			getLogger().info("Enabling SkyWorld generator for world " + worldName);
		});
	}

	private void loadDynamicDecorations(WorldDecorator worldDecorator, World world) {
		for (DecorationSettings settings : skyWorldConfig.getDecorationSettingsMap().values()) {
			DynamicDecoration dynamicDecoration = null;

			// TODO Any way to deal with the giant switch statement?
			switch (settings.placementType) {
				case AIR:
					dynamicDecoration = new AirDecoration(settings.name, settings.schematicName, world, skyWorldConfig);
					break;
				case FLOOR:
					break;
				case CEILING:
					break;
				case WALL:
					break;
			}

			if (dynamicDecoration != null) {
				worldDecorator.withCustomDecoration(DecorationType.SURFACE_STRUCTURES, dynamicDecoration);
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String cmdStr = command.getName();
		if (!cmdStr.equalsIgnoreCase("skyworld")) {
			return false;
		}

		commandHandler.manageCommand(sender, CommandHandler.cmdArgs(args));

		return true;
	}

	private void pasteCmd(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		if (args.length < 2) {
			sender.sendMessage("Usage: /sw paste schematicName x y z");
			return;
		}

		String decorationName = args[1];

		int x, y, z;
		if (args.length >= 4) {
			x = Integer.valueOf(args[2]);
			y = Integer.valueOf(args[3]);
			z = Integer.valueOf(args[4]);
		} else {
			x = (int) Math.floor(player.getLocation().getX());
			y = (int) Math.floor(player.getLocation().getY());
			z = (int) Math.floor(player.getLocation().getZ());
		}

		SkyWorld.getInstance().getDecorationManager().pasteSchematic(decorationName, player.getWorld(), x, y, z);

		sender.sendMessage("Pasted " + decorationName + " at " + x + " " + y + " " + z);
	}

	private void setupFileStructure() {
		File schematicsDirectory = new File(getDataFolder() + File.separator + "schematics" + File.separator);
		if (!schematicsDirectory.exists()) {
			schematicsDirectory.mkdirs();
		}
	}
}
