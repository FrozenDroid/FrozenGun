package com.frozendroid.beargun.models.objectives;
import com.frozendroid.beargun.BearGun;
import com.frozendroid.beargun.events.PlayerShotEvent;
import com.frozendroid.beargun.interfaces.GameObjective;
import com.frozendroid.beargun.models.Kill;
import com.frozendroid.beargun.models.Match;
import com.frozendroid.beargun.models.MinigamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Stream;

public class TotalKillObjective extends GameObjective implements Listener {

    private boolean achieved = false;

    private Integer killGoal;

    public void reset()
    {
        kills.clear();
    }



    @EventHandler
    public void onPlayerShot(PlayerShotEvent event)
    {
        super.onPlayerShot(event);

        if (event.getVictim().getPlayer().getHealth()-event.getGun().getDamage() > 0)
            return;

        if (match.isEnded())
            return;

        int total = 0;

        for (MinigamePlayer player : kills.keySet()) {
            total += kills.get(player).size();
        }

        if (total >= killGoal) {
            match.end();
        }
    }

    public String getEndText()
    {
        Comparator<MinigamePlayer> byKills = (p1, p2) -> Integer.compare(kills.get(p2).size(), kills.get(p1).size());
        Stream<MinigamePlayer> stream =  kills.keySet().stream().sorted(byKills);
        MinigamePlayer player = stream.findFirst().orElse(null);
        if (player == null) {
            return "The game at "+match.getArena().getName()+" ended.";
        }
        return player.getPlayer().getName() + " won the game at "+match.getArena().getName();
    }

    public String getTypeName()
    {
        return "total_kills";
    }

    public Integer getGoal()
    {
        return killGoal;
    }

    @Override
    public void removePlayer(MinigamePlayer player)
    {
        kills.remove(player);
    }

    public void setGoal(Integer i)
    {
        killGoal = i;
    }

    public void start()
    {
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
