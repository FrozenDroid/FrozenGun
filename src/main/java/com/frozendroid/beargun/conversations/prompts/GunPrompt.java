package com.frozendroid.beargun.conversations.prompts;

import com.frozendroid.beargun.Messenger;
import com.frozendroid.beargun.WeaponManager;
import com.frozendroid.beargun.models.Arena;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class GunPrompt extends StringPrompt {
    @Override
    public String getPromptText(ConversationContext conversationContext)
    {
        return Messenger.infoMsg("Please enter the name for the gun to use for this arena.");
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s)
    {
        Arena arena = (Arena) conversationContext.getSessionData("arena");
        if (s.equals(""))
            return this;

        arena.addWeapon(WeaponManager.findByName(s));
        return new ObjectivePrompt();
    }
}
