package com.frozendroid.frozengun.commands;

import com.frozendroid.frozengun.Messenger;
import com.frozendroid.frozengun.MinigameManager;
import com.frozendroid.frozengun.models.Match;
import com.frozendroid.frozengun.models.MinigamePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand {

    public static void run(CommandSender sender)
    {
        if (!(sender instanceof Player)) {
            return;
        }

        MinigamePlayer player = MinigameManager.getPlayer((Player) sender);
        if (player == null) {
            return;
        }

        if (player.inQueue()) {
            player.getQueue().removePlayer(player);
            player.sendMessage(Messenger.infoMsg("Left queue for arena " + player.getQueue().getArena().getName() + "!"));
            return;
        }

        Match match = player.getMatch();

        if (match == null) {
            return;
        }

        match.leave(player);
        player.sendMessage(Messenger.infoMsg("Left match in arena " + match.getArena().getName() + "!"));
    }

}
