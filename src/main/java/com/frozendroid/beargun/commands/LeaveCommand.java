package com.frozendroid.beargun.commands;

import com.frozendroid.beargun.MinigameManager;
import com.frozendroid.beargun.models.Match;
import com.frozendroid.beargun.models.MinigamePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand {

    public static void run(CommandSender sender)
    {
        if (!(sender instanceof Player)) {
            return;
        }

        Player player = (Player) sender;
        MinigamePlayer _player = MinigameManager.getPlayer(player);
        if (_player == null) {
            return;
        }
        Match match = _player.getMatch();

        if (match == null) {
            return;
        }

//        _player.leave(match);
    }

}
