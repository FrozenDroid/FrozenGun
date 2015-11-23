package com.frozendroid.beargun.conversations.prompts;

import com.frozendroid.beargun.interfaces.GameObjective;
import com.frozendroid.beargun.models.Arena;
import com.frozendroid.beargun.models.objectives.KillObjective;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObjectivePrompt extends StringPrompt {
    @Override
    public String getPromptText(ConversationContext conversationContext)
    {
        return "Enter the objectives that this arena will have.";
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s)
    {
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            System.out.println(matcher.group(1));
        } else {
            return this;
        }

        if (s.contains("total_kills")) {
            GameObjective objective = new KillObjective();
        } else {
            return this;
        }



        return new SpawnPrompt();
    }
}
