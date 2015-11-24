package com.frozendroid.beargun.interfaces;

import com.frozendroid.beargun.models.Match;

public interface GameObjective {

    boolean achieved = false;
    Match match = null;

    void setMatch(Match match);
    Match getMatch();

    String getEndText();
    String getTypeName();
    Object getGoal();
    void setGoal(Integer i);
    void start();
    void stop();
    void reset();

}
