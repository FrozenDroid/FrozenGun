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

public class TotalKillObjective implements GameObjective, Listener {

    private boolean achieved = false;
    private Match match;
    private HashMap<MinigamePlayer, Integer> kills = new HashMap<>();
    private Integer killGoal;

    public void reset()
    {
        kills = new HashMap<>();
    }

    @EventHandler
    public void onPlayerKill(PlayerShotEvent event)
    {
        if (event.getVictim().getPlayer().getHealth()-event.getGun().getDamage() > 0)
            return;
        kills.putIfAbsent(event.getShooter(), 0);
        kills.replace(event.getShooter(), kills.get(event.getShooter())+1);
        int total = 0;
        for (Integer value : kills.values()) { total+=value; }
        if (total >= getGoal()) {
            match.end();
        }
    }

    public String getEndText()
    {
        Comparator<MinigamePlayer> byKills = (p1, p2) -> Integer.compare(kills.get(p2), kills.get(p1));
        Stream<MinigamePlayer> stream =  kills.keySet().stream().sorted(byKills);
        return stream.findFirst().get().getPlayer().getDisplayName() + " won!";
    }

    public String getTypeName()
    {
        return "total_kills";
    }

    public Integer getGoal()
    {
        return killGoal;
    }

    public void setGoal(Integer i)
    {
        killGoal = i;
    }

    public void start()
    {
        match.getPlayers().forEach((player) -> kills.put(player, 0));
        BearGun.plugin.getServer().getPluginManager().registerEvents(this, BearGun.plugin);
    }

    public void stop()
    {
        PlayerShotEvent.getHandlerList().unregister(this);
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

}
