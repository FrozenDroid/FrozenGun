package com.frozendroid.frozengun.conversations.prompts;

import com.frozendroid.frozengun.Messenger;
import com.frozendroid.frozengun.MinigameManager;
import com.frozendroid.frozengun.models.Arena;
import com.frozendroid.frozengun.models.Spawn;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class SpawnPrompt extends StringPrompt {
    @Override
    public String getPromptText(ConversationContext conversationContext)
    {
        return Messenger.infoMsg("Stand at a spawn location, then type \"add\" to add the location to the arena. " +
                "Type \"delete\" to remove the last added spawn.");
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s)
    {
        Arena arena = (Arena) conversationContext.getSessionData("arena");
        if (s.equalsIgnoreCase("add")) {
            Spawn spawn = Spawn.fromLocation(((Player) conversationContext.getForWhom()).getLocation());
            arena.addSpawn(spawn);
            return this;
        }

        if (s.equalsIgnoreCase("done")) {
            arena.save();
            MinigameManager.addArena(arena);
        }
        return null;
    }
}
