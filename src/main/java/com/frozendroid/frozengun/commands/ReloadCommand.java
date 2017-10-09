package com.frozendroid.frozengun.commands;

import com.frozendroid.frozengun.Messenger;
import com.frozendroid.frozengun.MinigameManager;
import com.frozendroid.frozengun.configs.ArenaConfig;
import com.frozendroid.frozengun.configs.WeaponConfig;
import org.bukkit.command.CommandSender;

public class ReloadCommand {

    public static void run(CommandSender sender)
    {
        MinigameManager.endAllLobbies();
        MinigameManager.endAllMatches();

        MinigameManager.reset();
        WeaponConfig.loadGuns();
        ArenaConfig.loadArenas();

        sender.sendMessage(Messenger.infoMsg("Reloaded!"));
    }

}
