package com.frozendroid.beargun.conversations.prompts;

import com.frozendroid.beargun.MinigameManager;
import com.frozendroid.beargun.models.Arena;
import com.frozendroid.beargun.models.Gun;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class GunPrompt extends StringPrompt {
    @Override
    public String getPromptText(ConversationContext conversationContext)
    {
        return "Please enter the name for the gun to use for this arena.";
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s)
    {
        Arena arena = (Arena) conversationContext.getSessionData("arena");
        if (s.equals(""))
            return this;

        arena.setGun(Gun.findByName(s));
        return new ObjectivePrompt();
    }
}