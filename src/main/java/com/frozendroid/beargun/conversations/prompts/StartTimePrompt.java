package com.frozendroid.beargun.conversations.prompts;

import com.frozendroid.beargun.Messenger;
import com.frozendroid.beargun.models.Arena;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;

public class StartTimePrompt extends NumericPrompt {

    @Override
    public String getPromptText(ConversationContext conversationContext)
    {
        return Messenger.infoMsg("Insert a time (in seconds) for which to wait after the minimum" +
                " amount of players has been reached.");
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, Number number)
    {
        Arena arena = (Arena) conversationContext.getSessionData("arena");

        if (number.intValue() < 0)
            return this;

        arena.setStartingTime(number.intValue());

        return new SpawnPrompt();
    }

}
