package com.frozendroid.frozengun.conversations.prompts;

import com.frozendroid.frozengun.Messenger;
import com.frozendroid.frozengun.models.Arena;
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
    public boolean isNumberValid(ConversationContext conversationContext, Number number)
    {
        if (number.intValue() < 0)
            return false;
        return true;
    }

    @Override
    public String getFailedValidationText(ConversationContext conversationContext, Number number)
    {
        return "The wait time can not be lower than 0";
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, Number number)
    {
        Arena arena = (Arena) conversationContext.getSessionData("arena");

        arena.getLobby().setCountdownTime(number.intValue());

        return new SpawnPrompt();
    }

}
