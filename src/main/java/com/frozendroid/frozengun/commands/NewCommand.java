package com.frozendroid.frozengun.commands;

import com.frozendroid.frozengun.FrozenGun;
import com.frozendroid.frozengun.conversations.prompts.ArenaNamePrompt;
import com.frozendroid.frozengun.models.Arena;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class NewCommand {

    public static void run(CommandSender sender) {
        if (!(sender instanceof Player))
            return;

        if (!sender.hasPermission("frozengun.create.arena")) {
            return;
        }

        Player player = (Player) sender;
        Map<Object, Object> map = new HashMap<>();
        map.put("arena", Arena.create());
        Conversation conversation = new Conversation(FrozenGun.plugin, player, new ArenaNamePrompt(), map);
        conversation.setLocalEchoEnabled(false);
        player.beginConversation(conversation);
    }

}
