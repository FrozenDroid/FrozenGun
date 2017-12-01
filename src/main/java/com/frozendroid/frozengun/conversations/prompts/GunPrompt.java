package com.frozendroid.frozengun.conversations.prompts;

import com.frozendroid.frozengun.Messenger;
import com.frozendroid.frozengun.WeaponManager;
import com.frozendroid.frozengun.models.Arena;
import com.frozendroid.frozengun.models.Weapon;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

public class GunPrompt extends ValidatingPrompt {
    @Override
    public String getPromptText(ConversationContext context)
    {
        return Messenger.infoMsg("Please enter the name for the gun to use for this arena. (Must be defined in the weapons.yml)");
    }

    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        if (input.isEmpty())
            return false;

        Weapon weapon = WeaponManager.findByName(input);

        if (weapon == null) {
            return false;
        }
        return true;
    }

    @Override
    public String getFailedValidationText(ConversationContext context, String input)
    {
        return "That gun does not exist! Check your weapons.yml and try again.";
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, String input) {
        Arena arena = (Arena) context.getSessionData("arena");

        Weapon weapon = WeaponManager.findByName(input);
        arena.addWeapon(weapon);

        return new ObjectivePrompt();
    }
}
