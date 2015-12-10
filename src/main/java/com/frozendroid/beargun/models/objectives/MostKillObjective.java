package com.frozendroid.beargun.models.objectives;

import com.frozendroid.beargun.BearGun;
import com.frozendroid.beargun.Messenger;
import com.frozendroid.beargun.events.PlayerShotEvent;
import com.frozendroid.beargun.interfaces.GameObjective;
import com.frozendroid.beargun.interfaces.KillingSpree;
import com.frozendroid.beargun.models.Kill;
import com.frozendroid.beargun.models.Match;
import com.frozendroid.beargun.models.MinigamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Stream;

public class MostKillObjective extends GameObjective implements Listener {

    private Integer killGoal;
    private HashMap<MinigamePlayer, ArrayList<Kill>> kills = new HashMap<>();

    @EventHandler
    public void onPlayerShot(PlayerShotEvent event)
    {
        Bukkit.getScheduler().runTaskLater(BearGun.plugin, () -> {
            for (MinigamePlayer player : kills.keySet()) {
                if (kills.get(player).size() >= killGoal) {
                    match.end();
                    return;
                }
            }
        }, 1L);
    }


    public void setMatch(Match match)
    {
        this.match = match;
    }

    public Match getMatch()
    {
        return match;
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
        return "most_kills";
    }

    public Object getGoal()
    {
        return killGoal;
    }

    public void removePlayer(MinigamePlayer player)
    {
        kills.remove(player);
    }

    public void setGoal(Integer i)
    {
        this.killGoal = i;
    }

    public void start()
    {
        BearGun.plugin.getServer().getPluginManager().registerEvents(this, BearGun.plugin);
    }

    public void stop()
    {
        PlayerShotEvent.getHandlerList().unregister(this);
    }

    public void reset()
    {
        kills.clear();
    }
}
