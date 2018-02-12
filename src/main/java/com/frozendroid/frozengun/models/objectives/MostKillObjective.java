package com.frozendroid.frozengun.models.objectives;

import com.frozendroid.frozengun.FrozenGun;
import com.frozendroid.frozengun.events.PlayerShotEvent;
import com.frozendroid.frozengun.models.Match;
import com.frozendroid.frozengun.models.MinigamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class MostKillObjective extends GameObjective implements Listener {

    private Integer killGoal;

    @EventHandler
    public void onPlayerShot(PlayerShotEvent event)
    {
        super.onPlayerShot(event);

        for (MinigamePlayer player : kills.keySet()) {
            if (kills.get(player).size() >= killGoal) {
                match.end();
                return;
            }
        }
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
        ArrayList<MinigamePlayer> winners = getWinners();
        if (winners.size() == 0) {
            return "The game at " + match.getArena() + " ended.";
        }

        return winners.get(0).getDisplayName() + " won the game at " + match.getArena().getName() + "!";
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
        FrozenGun.plugin.getServer().getPluginManager().registerEvents(this, FrozenGun.plugin);
    }

    public void stop()
    {
        PlayerShotEvent.getHandlerList().unregister(this);
    }

    public void reset()
    {
        kills.clear();
    }

    /**
     * @return The one player that got the most kills.
     */
    @Override
    public ArrayList<MinigamePlayer> getWinners() {
        ArrayList<MinigamePlayer> winners = new ArrayList<>();
        Comparator<MinigamePlayer> byKills = (p1, p2) -> Integer.compare(kills.get(p2).size(), kills.get(p1).size());
        Optional<MinigamePlayer> winner = kills.keySet().stream().min(byKills);
        winner.ifPresent(winners::add);
        return winners;
    }
}
