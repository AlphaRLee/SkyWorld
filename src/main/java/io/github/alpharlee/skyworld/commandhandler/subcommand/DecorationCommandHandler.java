package io.github.alpharlee.skyworld.commandhandler.subcommand;

import io.github.alpharlee.skyworld.SkyWorld;
import io.github.alpharlee.skyworld.SkyWorldConfig;
import io.github.alpharlee.skyworld.commandhandler.CommandHandler;
import io.github.alpharlee.skyworld.commandhandler.SubcommandHandler;
import io.github.alpharlee.skyworld.decoration.DecorationSettings;
import io.github.alpharlee.skyworld.decoration.PlacementType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;

public class DecorationCommandHandler implements SubcommandHandler {
	private class HelpCommand implements SubcommandHandler {
		@Override
		public boolean onSubcommand(CommandSender sender, String[] args) {
			CommandHandler.sendMessage(sender, ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/skyworld decoration <OPTION> <DECORATION> [VALUE]",
					ChatColor.YELLOW + "/skyworld dec list",
					ChatColor.YELLOW + "/skyworld dec add myDecoration",
					ChatColor.YELLOW + "/skyworld dec remove myDecoration",
					ChatColor.YELLOW + "/skyworld dec type myDecoration air",
					ChatColor.YELLOW + "/skyworld dec schematic myDecoration my_schem_file",
					ChatColor.YELLOW + "/skyworld dec spawnchance myDecoration 0.01",
					ChatColor.YELLOW + "/skyworld dec spawnattempts myDecoration 5"
			);

			return true;
		}
	}

	private Map<String, SubcommandHandler> subcommands;
	private final HelpCommand helpCommand;

	public DecorationCommandHandler() {
		subcommands = new HashMap<>();
		helpCommand = new HelpCommand();

		SkyWorldConfig skyWorldConfig = SkyWorld.getInstance().getSkyWorldConfig();

		CommandHandler.assignCommand(subcommands, helpCommand, "help", "?");

		// List command
		CommandHandler.assignCommand(subcommands, (sender, args) -> {
			Map<String, DecorationSettings> decorations = skyWorldConfig.getDecorationSettingsMap();

			CommandHandler.sendMessage(sender, ChatColor.YELLOW + "Decorations in SkyWorld:");
			for (DecorationSettings settings : decorations.values()) {
				CommandHandler.sendMessage(sender, false, ChatColor.YELLOW + "- " + ChatColor.WHITE + settings.name);
			}

			return true;
		}, "list");

		// Add command
		CommandHandler.assignCommand(subcommands, (sender, args) -> {
			if (args.length < 1) {
				CommandHandler.sendError(sender, "Usage: /skyworld decoration add <NAME>");
				return false;
			}

			String name = args[0];
			if (skyWorldConfig.getDecorationSettingsMap().containsKey(name.toLowerCase())) {
				CommandHandler.sendError(sender, "Error: The decoration " + ChatColor.WHITE + name + ChatColor.RED + " already exists!");
				return false;
			}

			skyWorldConfig.addDefaultDecorationSetting(name);
			CommandHandler.sendMessage(sender, ChatColor.WHITE + name + ChatColor.GREEN + " has been added!",
					ChatColor.YELLOW + "You need to " + ChatColor.BOLD + "set the schematic" + ChatColor.YELLOW + " next, by typing: ",
					ChatColor.WHITE + "/skyworld decoration schematic " + name + " schematic_name_here");

			return true;
		}, "add");

		// Remove command
		CommandHandler.assignCommand(subcommands, (sender, args) -> {
			if (args.length < 1) {
				CommandHandler.sendError(sender, "Usage: /skyworld decoration remove <NAME>");
				return false;
			}

			String name = args[0];
			DecorationSettings settings = skyWorldConfig.getDynamicDecorationSettings(name);
			if (settings == null) {
				CommandHandler.sendError(sender, "Error: Could not find decoration named " + ChatColor.WHITE + name + ChatColor.RED + ".");
				return false;
			}

			skyWorldConfig.removeDecorationSettings(settings);
			CommandHandler.sendMessage(sender, ChatColor.WHITE + name + ChatColor.GREEN + " has been " + ChatColor.RED + "deleted!",
					ChatColor.YELLOW + "You must " + ChatColor.BOLD + "restart your server" + ChatColor.YELLOW + " for changes to take effect.");

			return true;
		}, "remove", "delete");

		// Placement type command
		CommandHandler.assignCommand(subcommands, (sender, args) -> {
			List<String> validTypeNames = Arrays.stream(PlacementType.values())
					.map(e -> e.getName().toUpperCase())
					.collect(Collectors.toList());

			if (args.length < 1) {
				CommandHandler.sendError(sender, "Usage: /skyworld decoration type <NAME> [" + String.join(" | ", validTypeNames) + "]");
				return false;
			}

			String name = args[0];
			DecorationSettings settings = skyWorldConfig.getDynamicDecorationSettings(name);
			if (settings == null) {
				CommandHandler.sendError(sender, "Error: Could not find decoration named " + ChatColor.WHITE + name + ChatColor.RED + ".");
				return false;
			}

			if (args.length < 2) {
				// Get request
				String typeStr = settings.placementType.getName();
				CommandHandler.sendMessage(sender, ChatColor.YELLOW + "Decoration " + ChatColor.WHITE + settings.name
						+ ChatColor.YELLOW + " has placement type " + ChatColor.WHITE + typeStr + ChatColor.YELLOW + ".");

				return true;
			}

			// Verify input placementType
			String value = args[1];
			PlacementType placementType = PlacementType.get(value.toLowerCase());
			if (placementType == null) {
				CommandHandler.sendError(sender, "Error, placement type " + ChatColor.WHITE + value + ChatColor.RED + " not recognized.",
						ChatColor.RED + "Valid options are: " + String.join(", ", validTypeNames));
				return false;
			}

			// Set placement type
			skyWorldConfig.setDecorationConfigSetting(name, "type", placementType.getName().toLowerCase());
			CommandHandler.sendMessage(sender, ChatColor.GREEN + "Placement type of " + settings.name
							+ ChatColor.GREEN + " has been set to " + ChatColor.WHITE + placementType.getName().toUpperCase() + ChatColor.GREEN + ".",
					ChatColor.YELLOW + "You must " + ChatColor.BOLD + "restart your server" + ChatColor.YELLOW + " for changes to take effect.");

			return true;
		}, "type");

		// Schematic command
		CommandHandler.assignCommand(subcommands, (sender, args) -> {
			if (args.length < 1) {
				CommandHandler.sendError(sender, "Usage: /skyworld decoration schematic <NAME> [SCHEMATIC]");
				return false;
			}

			String name = args[0];
			DecorationSettings settings = skyWorldConfig.getDynamicDecorationSettings(name);
			if (settings == null) {
				CommandHandler.sendError(sender, "Error: Could not find decoration named " + ChatColor.WHITE + name + ChatColor.RED + ".");
				return false;
			}

			if (args.length < 2) {
				// Get request

				CommandHandler.sendMessage(sender, ChatColor.YELLOW + "Decoration " + ChatColor.WHITE + settings.name
						+ ChatColor.YELLOW + " has schematic " + ChatColor.WHITE + settings.schematicName + ChatColor.YELLOW + ".");
				return true;
			}

			// Get input of placement type
			String value = args[1];

			// Set schematic
			skyWorldConfig.setDecorationConfigSetting(name, "schematic", value);
			CommandHandler.sendMessage(sender, ChatColor.GREEN + "Schematic of " + settings.name
							+ ChatColor.GREEN + " has been set to " + ChatColor.WHITE + value + ChatColor.GREEN + ".",
					ChatColor.YELLOW + "You must " + ChatColor.BOLD + "restart your server" + ChatColor.YELLOW + " for changes to take effect.");

			return true;
		}, "schematic", "schem");

		// Chance command
		CommandHandler.assignCommand(subcommands, (sender, args) -> {
			if (args.length < 1) {
				CommandHandler.sendError(sender, "Usage: /skyworld decoration chance <NAME> [PROBABILITY]");
				return false;
			}

			String name = args[0];
			DecorationSettings settings = skyWorldConfig.getDynamicDecorationSettings(name);
			if (settings == null) {
				CommandHandler.sendError(sender, "Error: Could not find decoration named " + ChatColor.WHITE + name + ChatColor.RED + ".");
				return false;
			}

			if (args.length < 2) {
				// Get request

				CommandHandler.sendMessage(sender, ChatColor.YELLOW + "Decoration " + ChatColor.WHITE + settings.name
						+ ChatColor.YELLOW + " has spawn chance of " + ChatColor.WHITE + settings.spawnChance + ChatColor.YELLOW + ".");
				return true;
			}

			// Get input of spawn chance
			String strValue = args[1];
			double spawnChance;
			try {
				spawnChance = CommandHandler.toDouble(strValue, 0d, 1d);
			} catch (IllegalArgumentException e) {
				CommandHandler.sendError(sender, e.getMessage());
				return false;
			}

			// Set spawn chance
			skyWorldConfig.setDynamicDecorationProperty(name, "spawnChance", (float) spawnChance);
			CommandHandler.sendMessage(sender, ChatColor.GREEN + "Spawn chance of " + settings.name
							+ ChatColor.GREEN + " has been set to " + ChatColor.WHITE + spawnChance + ChatColor.GREEN + ".",
					ChatColor.YELLOW + "You must " + ChatColor.BOLD + "restart your server" + ChatColor.YELLOW + " for changes to take effect.");

			return true;
		}, "spawnchance", "chance", "probability");

		// Spawn attempts command
		CommandHandler.assignCommand(subcommands, (sender, args) -> {
			if (args.length < 1) {
				CommandHandler.sendError(sender, "Usage: /skyworld decoration chance <NAME> [SPAWN ATTEMPTS]");
				return false;
			}

			String name = args[0];
			DecorationSettings settings = skyWorldConfig.getDynamicDecorationSettings(name);
			if (settings == null) {
				CommandHandler.sendError(sender, "Error: Could not find decoration named " + ChatColor.WHITE + name + ChatColor.RED + ".");
				return false;
			}

			if (args.length < 2) {
				// Get request

				CommandHandler.sendMessage(sender, ChatColor.YELLOW + "Decoration " + ChatColor.WHITE + settings.name
						+ ChatColor.YELLOW + " has spawn attempts of " + ChatColor.WHITE + settings.spawnAttempts + ChatColor.YELLOW + ".");
				return true;
			}

			// Get input of spawn chance
			String strValue = args[1];
			int spawnAttempts;
			try {
				spawnAttempts = CommandHandler.toInt(strValue, 0, null);
			} catch (IllegalArgumentException e) {
				CommandHandler.sendError(sender, e.getMessage());
				return false;
			}

			// Set spawn attempts
			skyWorldConfig.setDynamicDecorationProperty(name, "spawnAttempts", (float) spawnAttempts);
			CommandHandler.sendMessage(sender, ChatColor.GREEN + "Spawn attempts of " + settings.name
					+ ChatColor.GREEN + " has been set to " + ChatColor.WHITE + spawnAttempts + ChatColor.GREEN + ".",
					ChatColor.YELLOW + "You must " + ChatColor.BOLD + "restart your server" + ChatColor.YELLOW + " for changes to take effect.");

			return true;
		}, "attempts", "spawnattempts");
	}

	@Override
	public boolean onSubcommand(CommandSender sender, String[] args) {
		if (args.length < 1) {
			return helpCommand.onSubcommand(sender, CommandHandler.cmdArgs(args));
		}

		SubcommandHandler subcommand = subcommands.get(args[0].toLowerCase());

		if (subcommand == null) {
			CommandHandler.sendError(sender, "Please type" + ChatColor.WHITE + "/skyworld decoration help" + ChatColor.RED + " for a list of commands.");
			return false;
		}

		return subcommand.onSubcommand(sender, CommandHandler.cmdArgs(args));
	}
}
