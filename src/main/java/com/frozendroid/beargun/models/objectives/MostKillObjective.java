package com.frozendroid.beargun.models.objectives;

import com.frozendroid.beargun.BearGun;
import com.frozendroid.beargun.events.PlayerShotEvent;
import com.frozendroid.beargun.interfaces.GameObjective;
import com.frozendroid.beargun.models.Match;
import com.frozendroid.beargun.models.MinigamePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Stream;

public class MostKillObjective implements GameObjective, Listener {

    private Match match;
    private Integer killGoal;
    private HashMap<MinigamePlayer, Integer> kills = new HashMap<>();

    @EventHandler
    public void onPlayerShot(PlayerShotEvent event)
    {
        if (event.getVictim().getPlayer().getHealth()-event.getGun().getDamage() > 0)
            return;
        kills.putIfAbsent(event.getShooter(), 0);
        kills.replace(event.getShooter(), kills.get(event.getShooter())+1);
        kills.forEach((player, kills) -> {
            if (kills >= killGoal) {
                match.end();
            }
        });
    }

    @Override
    public void setMatch(Match match)
    {
        this.match = match;
    }

    @Override
    public Match getMatch()
    {
        return match;
    }

    @Override
    public String getEndText()
    {
        Comparator<MinigamePlayer> byKills = (p1, p2) -> Integer.compare(kills.get(p2), kills.get(p1));
        Stream<MinigamePlayer> stream =  kills.keySet().stream().sorted(byKills);
        return stream.findFirst().get().getPlayer().getName() + " won the game at "+match.getArena().getName();
    }

    @Override
    public String getTypeName()
    {
        return "most_kills";
    }

    @Override
    public Object getGoal()
    {
        return killGoal;
    }

    @Override
    public void setGoal(Integer i)
    {
        this.killGoal = i;
    }

    @Override
    public void start()
    {
        BearGun.plugin.getServer().getPluginManager().registerEvents(this, BearGun.plugin);
    }

    @Override
    public void stop()
    {
        PlayerShotEvent.getHandlerList().unregister(this);
    }

    @Override
    public void reset()
    {
        killGoal = 0;
    }
}
