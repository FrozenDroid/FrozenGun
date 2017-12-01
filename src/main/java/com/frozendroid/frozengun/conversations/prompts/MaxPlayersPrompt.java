package com.frozendroid.frozengun.conversations.prompts;

import com.frozendroid.frozengun.Messenger;
import com.frozendroid.frozengun.models.Arena;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;

public class MaxPlayersPrompt extends NumericPrompt {

    @Override
    public String getPromptText(ConversationContext conversationContext)
    {
        return Messenger.infoMsg("Please enter the maximum amount of players that this arena can hold.");
    }

    @Override
    public boolean isNumberValid(ConversationContext context, Number number) {
        int maxPlayers = number.intValue();
        if (((Arena) context.getSessionData("arena")).getMinPlayers() > maxPlayers)
            return false;
        return true;
    }

    @Override
    public String getFailedValidationText(ConversationContext context, Number number)
    {
        return "Maximum player amount must be higher than minimum player amount.";
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, Number number)
    {
        Arena arena = (Arena) context.getSessionData("arena");

        arena.setMaxPlayers(number.intValue());

        return new GunPrompt();
    }

}
