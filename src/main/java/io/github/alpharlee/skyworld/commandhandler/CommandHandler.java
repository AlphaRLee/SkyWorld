package io.github.alpharlee.skyworld.commandhandler;

import io.github.alpharlee.skyworld.commandhandler.subcommand.DecorationCommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandHandler {
    private static Map<String, SubcommandHandler> subcommands;

    static {
        subcommands = new HashMap<>();
        alias(subcommands, new DecorationCommandHandler(), "decoration", "dec", "structure", "struct");
    }

    /**
     * Manage commands from any command sender
     *
     * @param sender
     * @param args
     * @return
     * @author R Lee
     */
    public boolean manageCommand(CommandSender sender, String[] args) {
        boolean result = true;

	    if (args.length < 1) {
		    sendError(sender, "Please type /skyworld help for a list of commands.");
		    return false;
	    }

	    SubcommandHandler subcommand = subcommands.get(args[0].toLowerCase());

        if (subcommand == null) {
            sendError(sender, "Please type /skyworld help for a list of commands.");
            return false;
        }

        subcommand.onSubcommand(sender, cmdArgs(args));

        return result;
    }

    /**
     * Send a message or a set of messages to a recipient (player or console), with the "[Dogfight]" prefix
     *
     * @param recipient Message receiver. Must either be a player or an instance of ConsoleCommandSender
     * @param messages  Messages to be given to receiver. If the recipient is a player then the first message will be prefixed with "[Dogfight]"
     * @author R Lee
     */
    public static void sendMessage(CommandSender recipient, String... messages) {
        sendMessage(recipient, true, messages);
    }

    /**
     * Send a message or a set of messages to a recipient (player or console)
     *
     * @param recipient  Message receiver. Must either be a player or an instance of ConsoleCommandSender. Null also works for a ConsoleCommandSender
     * @param showPrefix Set to true to include the "[Dogfight]" message prefix
     * @param messages   Messages to be given to receiver. If the recipient is a player then the first message will be prefixed with "[Dogfight]"
     * @author R Lee
     */
    public static void sendMessage(CommandSender recipient, boolean showPrefix, String... messages) {
        String prefix = "";

        if (showPrefix) {
            prefix = ChatColor.DARK_GREEN + "[" + ChatColor.AQUA + "SkyWorld" + ChatColor.DARK_GREEN + "] " + ChatColor.RESET;
        }

        if (recipient instanceof Player) {
            Player player = (Player) recipient;

            // Send first message wih prefix in front
            player.sendMessage(prefix + messages[0]);

            if (messages.length > 1) {
                // NOTE: Iterator starts at 1 (second message), NOT 0
                for (int i = 1; i < messages.length; i++) {
                    player.sendMessage(messages[i]);
                }
            }
        } else if (recipient instanceof ConsoleCommandSender || recipient == null) {
            Bukkit.getServer().getLogger().info(prefix + messages[0]);

            if (messages.length > 1) {
                //NOTE: Iterator starts at 1 (second message), NOT 0
                for (int i = 1; i < messages.length; i++) {
                    Bukkit.getServer().getLogger().info(messages[i]);
                }
            }
        }
    }

    /**
     * Performs same function as {@link CommandHandler#sendMessage(CommandSender, String...)}, except colours all messages red
     *
     * @param recipient
     * @param messages
     * @author R Lee
     */
    public static void sendError(CommandSender recipient, String... messages) {
        String[] errorMessages = new String[messages.length];

        for (int i = 0; i < errorMessages.length; i++) {
            errorMessages[i] = ChatColor.RED + messages[i];
        }

        sendMessage(recipient, errorMessages);
    }

    /**
     * Get the 2nd (index of 1) args and all args following that one
     *
     * @param args
     * @return
     * @author R Lee
     */
    public static String[] cmdArgs(String[] args) {
    	if (args.length <= 1) {
    		return new String[0];
	    }

        //For Arrays.copyOfRange:
        //1st parameter: Original array list
        //2nd parameter: From index (inclusive)
        //3rd parameter: To index (exclusive)
        return Arrays.copyOfRange(args, 1, args.length);
    }

    public static void alias(Map<String, SubcommandHandler> subcommandMap, SubcommandHandler subcommand, String... commandNames) {
    	for (String commandName : commandNames) {
    		subcommandMap.put(commandName, subcommand);
	    }
    }

    public static double toDouble(String value, Double min, Double max) throws IllegalArgumentException {
        double output;
        try {
            output = Double.valueOf(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ChatColor.RED + "Error: Cannot convert " + ChatColor.WHITE + value + ChatColor.RED + " to a number.");
        }

        String errorMsg = ChatColor.RED + "Error: Value must be ";
        if (min == null && max == null) {
        	return output;
        } else if (min == null && output > max) {
        	errorMsg += "less than " + ChatColor.WHITE + max + ChatColor.RED + ".";;
        } else if (output < min && max == null) {
	        errorMsg += "greater than " + ChatColor.WHITE + max + ChatColor.RED + ".";;
        } else {
        	if (output >= min && output <= max) {
        		return output;
	        } else {
        		errorMsg += "between " + ChatColor.WHITE + min + ChatColor.RED + " and " + ChatColor.WHITE + max + ChatColor.RED + ".";
	        }
        }

		throw new IllegalArgumentException(errorMsg);
    }

    public static int toInt(String value, Integer min, Integer max) throws IllegalArgumentException {
	    int output;
	    try {
		    output = Integer.valueOf(value);
	    } catch (NumberFormatException e) {
		    throw new IllegalArgumentException(ChatColor.RED + "Error: Cannot convert " + ChatColor.WHITE + value + ChatColor.RED + " to an integer.");
	    }

	    String errorMsg = ChatColor.RED + "Error: Value must be ";
	    if (min == null && max == null) {
		    return output;
	    } else if (min == null && output > max) {
		    errorMsg += "less than " + ChatColor.WHITE + max + ChatColor.RED + ".";;
	    } else if (output < min && max == null) {
		    errorMsg += "greater than " + ChatColor.WHITE + max + ChatColor.RED + ".";;
	    } else {
		    if (output >= min && output <= max) {
			    return output;
		    } else {
			    errorMsg += "between " + ChatColor.WHITE + min + ChatColor.RED + " and " + ChatColor.WHITE + max + ChatColor.RED + ".";
		    }
	    }

	    throw new IllegalArgumentException(errorMsg);
    }
}
