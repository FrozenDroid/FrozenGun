package com.frozendroid.frozengun.conversations.prompts;

import com.frozendroid.frozengun.Messenger;
import com.frozendroid.frozengun.models.Arena;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class ArenaNamePrompt extends StringPrompt {


    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return Messenger.infoMsg("Please enter a name for the new arena.");
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        Arena arena = (Arena) conversationContext.getSessionData("arena");
        if (s.equals(""))
            return this;


        arena.setName(s);

        return new MinPlayersPrompt();
    }
}
