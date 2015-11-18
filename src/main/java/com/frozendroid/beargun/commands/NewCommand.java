package com.frozendroid.beargun.commands;

import org.bukkit.command.CommandSender;

public class NewCommand {

    public static void run(String arenaName, CommandSender sender)
    {
        if (arenaName == null) {
            sender.sendMessage("Arena name cannot be empty.");
        }
    }

}
