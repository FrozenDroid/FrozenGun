package com.frozendroid.beargun.commands;

import com.frozendroid.beargun.Messenger;
import com.frozendroid.beargun.MinigameManager;
import com.frozendroid.beargun.configs.ArenaConfig;
import com.frozendroid.beargun.configs.WeaponConfig;
import org.bukkit.command.CommandSender;

public class ReloadCommand {

    public static void run(CommandSender sender)
    {
        MinigameManager.endAllQueues();
        MinigameManager.endAllMatches();

        MinigameManager.reset();
        WeaponConfig.loadGuns();
        ArenaConfig.loadArenas();

        sender.sendMessage(Messenger.infoMsg("Reloaded!"));
    }

}
