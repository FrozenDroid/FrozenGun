package com.frozendroid.beargun.conversations.prompts;

import com.frozendroid.beargun.interfaces.GameObjective;
import com.frozendroid.beargun.models.Arena;
import com.frozendroid.beargun.models.objectives.TotalKillObjective;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

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
        Arena arena = (Arena) conversationContext.getSessionData("arena");

        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(s);
        Integer goal = 0;
        if (matcher.find()) {
            goal = Integer.parseInt(matcher.group(1));
        } else {
            return this;
        }

        if (s.contains("total_kills") && goal != 0) {
            GameObjective objective = new TotalKillObjective();
            objective.setGoal(goal);
            arena.addObjective(objective);

        } else {
            return this;
        }



        return new StartTimePrompt();
    }
}
