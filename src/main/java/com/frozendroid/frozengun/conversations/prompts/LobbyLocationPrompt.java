package com.frozendroid.frozengun.conversations.prompts;

import com.frozendroid.frozengun.Messenger;
import com.frozendroid.frozengun.models.Arena;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

public class LobbyLocationPrompt extends ValidatingPrompt {
    @Override
    public String getPromptText(ConversationContext context) {
        return Messenger.infoMsg(
                "Type \"here\" to put the lobby location at your location, or type \"next\" to continue."
        );
    }

    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        if (input.equalsIgnoreCase("here") || input.equalsIgnoreCase("next")) {
            return true;
        }
        return false;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, String input) {
        Arena arena = (Arena) context.getSessionData("arena");

        if (input.equalsIgnoreCase("here")) {
            Player player = (Player) context.getForWhom();
            arena.getLobby().setLocation(player.getLocation());
        }

        return new StartTimePrompt();
    }
}
