package com.frozendroid.frozengun.conversations.prompts;

import com.frozendroid.frozengun.Messenger;
import com.frozendroid.frozengun.models.objectives.GameObjective;
import com.frozendroid.frozengun.models.Arena;
import com.frozendroid.frozengun.models.objectives.MostKillObjective;
import com.frozendroid.frozengun.models.objectives.TotalKillObjective;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObjectivePrompt extends StringPrompt {
    @Override
    public String getPromptText(ConversationContext conversationContext)
    {
        return Messenger.infoMsg("Enter the objectives that this arena will have.");
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

        GameObjective objective;
        if (s.contains("total_kills") && goal != 0) {
            objective = new TotalKillObjective();
        } else if (s.contains("most_kills") && goal != 0) {
            objective = new MostKillObjective();
        } else {
            return this;
        }

        objective.setGoal(goal);
        arena.setObjective(objective);

        return new LobbyLocationPrompt();
    }
}
