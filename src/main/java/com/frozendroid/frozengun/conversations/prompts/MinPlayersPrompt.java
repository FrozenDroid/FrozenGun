package com.frozendroid.frozengun.conversations.prompts;

import com.frozendroid.frozengun.Messenger;
import com.frozendroid.frozengun.models.Arena;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.NumericPrompt;

public class MinPlayersPrompt extends NumericPrompt {

    @Override
    public String getPromptText(ConversationContext conversationContext)
    {
        return Messenger.infoMsg("Please enter the minimum amount of players required to start the matches in this arena.");
    }

    @Override
    public boolean isNumberValid(ConversationContext conversationContext, Number number)
    {
        int maxPlayers = number.intValue();

        if (maxPlayers < 0)
            return false;
        return true;
    }

    @Override
    public String getFailedValidationText(ConversationContext conversationContext, Number number)
    {
        int maxPlayers = number.intValue();
        if (maxPlayers < 0)
            return "This amount can not be lower than 0";
        return "wut";
    }


    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, Number number)
    {
        Arena arena = (Arena) conversationContext.getSessionData("arena");
        arena.setMinPlayers(number.intValue());
        return new MaxPlayersPrompt();
    }

}
