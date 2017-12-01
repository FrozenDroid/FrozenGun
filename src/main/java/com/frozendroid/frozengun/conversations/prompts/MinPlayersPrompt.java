package com.frozendroid.frozengun.conversations.prompts;

import com.frozendroid.frozengun.Messenger;
import com.frozendroid.frozengun.models.Arena;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.NumericPrompt;

public class MinPlayersPrompt extends NumericPrompt {

    @Override
    public String getPromptText(ConversationContext context)
    {
        return Messenger.infoMsg("Please enter the minimum amount of players required to start the matches in this arena.");
    }

    @Override
    public boolean isNumberValid(ConversationContext context, Number number)
    {
        int maxPlayers = number.intValue();

        if (maxPlayers < 0)
            return false;
        return true;
    }

    @Override
    public String getFailedValidationText(ConversationContext context, Number number)
    {
        return "This amount can not be lower than 0";
    }


    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, Number number)
    {
        Arena arena = (Arena) context.getSessionData("arena");

        arena.setMinPlayers(number.intValue());

        return new MaxPlayersPrompt();
    }

}
