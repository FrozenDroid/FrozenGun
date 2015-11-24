package com.frozendroid.beargun.commands;

import com.frozendroid.beargun.BearGun;
import com.frozendroid.beargun.conversations.prompts.ArenaNamePrompt;
import com.frozendroid.beargun.models.Arena;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class NewCommand {

    public static void run(CommandSender sender)
    {
        if (!(sender instanceof Player))
            return;

        if (!sender.hasPermission("beargun.create.arena")) {
            return;
        }

        Player player = (Player) sender;
        Map<Object, Object> map = new HashMap<>();
        map.put("arena", Arena.create());
        Conversation conversation = new Conversation(BearGun.plugin, player, new ArenaNamePrompt(), map);
        conversation.setLocalEchoEnabled(false);
        player.beginConversation(conversation);
    }

}
