package com.frozendroid.frozengun.conversations.prompts;

import com.frozendroid.frozengun.Messenger;
import com.frozendroid.frozengun.MinigameManager;
import com.frozendroid.frozengun.models.Arena;
import com.frozendroid.frozengun.models.Spawn;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpawnPrompt extends StringPrompt {
    private int spawnCount = 0;

    @Override
    public String getPromptText(ConversationContext context)
    {
        return Messenger.infoMsg("Stand at a spawn location, then type \"add\" to add the location to the arena. " +
                "Type \"delete\" to remove the spawn. When done, type \"done\".");
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String s)
    {
        Arena arena = (Arena) context.getSessionData("arena");

        if (s.equalsIgnoreCase("add")) {
            Spawn spawn = Spawn.fromLocation(((Player) context.getForWhom()).getLocation());
            arena.addSpawn(spawn);
            context.getForWhom().sendRawMessage(
                    Messenger.infoMsg("Spawn #" + ++spawnCount + " added!")
            );
            return this;
        }

        else if (s.equalsIgnoreCase("delete")) {
            arena.removeSpawn(--spawnCount);
            context.getForWhom().sendRawMessage(
                    Messenger.infoMsg("Last created spawn removed!")
            );
            return this;
        }

        else if (s.equalsIgnoreCase("done")) {
            context.getForWhom().sendRawMessage(Messenger.infoMsg("Arena saved!"));
            arena.save();
            MinigameManager.addArena(arena);
        }
        return null;
    }
}
