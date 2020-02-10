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
			CommandHandler.sendMessage(sender, ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/skyworld decoration <OPTION> [VALUE] <DECORATION>",
					ChatColor.YELLOW + "/skyworld decoration list",
					ChatColor.YELLOW + "/skyworld decoration add myDecoration",
					ChatColor.YELLOW + "/skyworld decoration remove myDecoration",
					ChatColor.YELLOW + "/skyworld decoration type air myDecoration",
					ChatColor.YELLOW + "/skyworld decoration schematic my_thing_file myDecoration",
					ChatColor.YELLOW + "/skyworld decoration spawnchance 0.01 myDecoration",
					ChatColor.YELLOW + "/skyworld decoration spawnAttempts 5 myDecoration"
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

		CommandHandler.alias(subcommands, helpCommand, "help", "?");

		// List command
		CommandHandler.alias(subcommands, (sender, args) -> {
			Map<String, DecorationSettings> decorations = skyWorldConfig.getDecorationSettingsMap();

			CommandHandler.sendMessage(sender, ChatColor.YELLOW + "Decorations in SkyWorld:");
			for (DecorationSettings settings : decorations.values()) {
				CommandHandler.sendMessage(sender, false, ChatColor.YELLOW + "- " + ChatColor.WHITE + settings.name);
			}

			return true;
		}, "list");

		CommandHandler.alias(subcommands, (sender, args) -> {
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
					ChatColor.WHITE + "/skyworld decoration schematic schematic_name_here " + name);

			return true;
		}, "add");

		CommandHandler.alias(subcommands, (sender, args) -> {
			if (args.length < 1) {
				CommandHandler.sendError(sender, "Usage: /skyworld decoration remove <NAME>");
				return false;
			}

			// FIXME add code to delete
			CommandHandler.sendMessage(sender, ChatColor.LIGHT_PURPLE + "!!! This feature is not implemented yet");
			return true;
		}, "remove", "delete");

		CommandHandler.alias(subcommands, (sender, args) -> {
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
