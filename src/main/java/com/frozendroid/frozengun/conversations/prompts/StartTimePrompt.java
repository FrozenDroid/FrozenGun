package com.frozendroid.frozengun.conversations.prompts;

import com.frozendroid.frozengun.Messenger;
import com.frozendroid.frozengun.models.Arena;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;

public class StartTimePrompt extends NumericPrompt {

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return Messenger.infoMsg("Insert a time (in seconds) for which to wait after the minimum" +
                " amount of players has been reached.");
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, Number number) {
        Arena arena = (Arena) conversationContext.getSessionData("arena");

        if (number.intValue() < 0)
            return this;

        arena.getLobby().setCountdownTime(number.intValue());

        return new SpawnPrompt();
    }

}
