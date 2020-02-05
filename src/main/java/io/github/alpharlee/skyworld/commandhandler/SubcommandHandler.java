package io.github.alpharlee.skyworld.commandhandler;

import org.bukkit.command.CommandSender;

public interface SubcommandHandler {
    public boolean onSubcommand(CommandSender sender, String[] args);
}
