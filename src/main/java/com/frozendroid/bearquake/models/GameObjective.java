package com.frozendroid.bearquake.models;

public class GameObjective {

    protected Match match;

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public void complete(MinigamePlayer player)
    {
        match.broadcast(player.getPlayer().getDisplayName()+ " won!");
        match.end();
    }


}
