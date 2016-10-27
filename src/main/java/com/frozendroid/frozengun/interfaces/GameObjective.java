package com.frozendroid.frozengun.interfaces;

import com.frozendroid.frozengun.events.PlayerShotEvent;
import com.frozendroid.frozengun.models.Kill;
import com.frozendroid.frozengun.models.Match;
import com.frozendroid.frozengun.models.MinigamePlayer;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GameObjective implements Listener {

    boolean achieved = false;
    public Match match;

    public HashMap<MinigamePlayer, ArrayList<Kill>> kills = new HashMap<>();

    public abstract void setMatch(Match match);
    public abstract Match getMatch();

    public abstract String getEndText();
    public abstract String getTypeName();
    public abstract Object getGoal();
    public abstract void removePlayer(MinigamePlayer player);
    public abstract void setGoal(Integer i);
    public abstract void start();
    public abstract void stop();
    public abstract void reset();

    public void onPlayerShot(PlayerShotEvent event)
    {
        if (event.getVictim().getHealth()-event.getGun().getDamage() > 0)
            return;

        match.broadcast(event.getShooter().getName() + " killed " + event.getVictim().getName()+"!");

        Kill kill = new Kill();
        kill.setKilled(event.getVictim());
        kill.setKiller(event.getShooter());
        kill.setTime(System.currentTimeMillis());
        kills.putIfAbsent(event.getShooter(), new ArrayList<>());

        ArrayList<Kill> _kills = kills.get(event.getShooter());
        _kills.add(kill);

        if (_kills.size() > 1) {
            Kill lastKill = _kills.get(_kills.size() - 2);
            Kill currentKill = _kills.get(_kills.size() - 1);

            Long delta = currentKill.getTime() - lastKill.getTime();
            if (match.getArena().getKillingSpreeDelay() >= delta/1000) {
                currentKill.setSpree(lastKill.getSpree() + 1);

                if (currentKill.getSpree() == 2)
                    match.broadcast(event.getShooter().getDisplayName() + " got a double kill!");
                if (currentKill.getSpree() == 3)
                    match.broadcast(event.getShooter().getDisplayName() + " got a triple kill!");
                if (currentKill.getSpree() == 4)
                    match.broadcast(event.getShooter().getDisplayName() + " got a quadra kill!");
            }
        }
    }
}
