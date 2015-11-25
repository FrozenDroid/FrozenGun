package com.frozendroid.beargun.commands;

import com.frozendroid.beargun.MinigameManager;
import com.frozendroid.beargun.configs.ArenaConfig;
import com.frozendroid.beargun.configs.GunConfig;
import com.frozendroid.beargun.models.Match;
import org.bukkit.command.CommandSender;

public class ReloadCommand {

    public static void run(CommandSender sender)
    {
        MinigameManager.getMatches().forEach(Match::end);


        MinigameManager.reset();
        GunConfig.loadGuns();
        ArenaConfig.loadArenas();
    }

}
