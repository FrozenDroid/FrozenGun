package com.frozendroid.frozengun.conversations.prompts;

import com.frozendroid.frozengun.Messenger;
import com.frozendroid.frozengun.models.Arena;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class LobbyLocationPrompt extends StringPrompt {
    @Override
    public String getPromptText(ConversationContext context) {
        return Messenger.infoMsg(
                "Type \"here\" to put the lobby location at your location, or type \"next\" to continue."
        );
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        Arena arena = (Arena) context.getSessionData("arena");
        if (input.equalsIgnoreCase("here")) {
            Player player = (Player) context.getForWhom();
            arena.getLobby().setLocation(player.getLocation());
        }
        return new StartTimePrompt();
    }
}
