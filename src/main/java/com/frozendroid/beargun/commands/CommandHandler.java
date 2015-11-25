package com.frozendroid.beargun.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class CommandHandler implements CommandExecutor {

    private Plugin plugin;

    public CommandHandler(Plugin plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginCommand("beargun").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("arena") && args[1].equalsIgnoreCase("new")) {
                NewCommand.run(sender);
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("leave")) {
                LeaveCommand.run(sender);
            } else if (args[0].equalsIgnoreCase("reload")) {
                ReloadCommand.run(sender);
            }
        }
        return true;
    }

}
