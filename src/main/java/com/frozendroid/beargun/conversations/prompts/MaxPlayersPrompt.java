package com.frozendroid.beargun.conversations.prompts;

import com.frozendroid.beargun.models.Arena;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;

public class MaxPlayersPrompt extends NumericPrompt {

    @Override
    public String getPromptText(ConversationContext conversationContext)
    {
        return "Please enter the maximum amount of players that this arena can hold.";
    }

    @Override
    public boolean isNumberValid(ConversationContext conversationContext, Number number)
    {
        int maxPlayers = number.intValue();

        if (maxPlayers < 0 || ((Arena) conversationContext.getSessionData("arena")).getMinPlayers() > maxPlayers)
            return false;
        return true;
    }

    @Override
    public String getFailedValidationText(ConversationContext conversationContext, Number number)
    {
        int maxPlayers = number.intValue();
        if (maxPlayers < 0)
            return "This amount can not be lower than 0";
        if (((Arena) conversationContext.getSessionData("arena")).getMinPlayers() > maxPlayers)
            return "Maximum players amount must be higher than minimum players amount.";
        return "wut";
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, Number number)
    {
        Arena arena = (Arena) conversationContext.getSessionData("arena");
        int maxPlayers = number.intValue();
        arena.setMaxPlayers(maxPlayers);
        return new GunPrompt();
    }

}
