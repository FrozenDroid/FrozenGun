package com.frozendroid.frozengun.conversations.prompts;

import com.frozendroid.frozengun.Messenger;
import com.frozendroid.frozengun.models.Arena;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

public class ArenaNamePrompt extends ValidatingPrompt {
    @Override
    public String getPromptText(ConversationContext context)
    {
        return Messenger.infoMsg("Please enter a name for the new arena.");
    }

    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        if (input.isEmpty())
            return false;
        return true;
    }

    @Override
    public String getFailedValidationText(ConversationContext context, String input)
    {
        return "The arena name cannot be empty";
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, String input) {
        Arena arena = (Arena) context.getSessionData("arena");

        arena.setName(input);

        return new MinPlayersPrompt();
    }
}
