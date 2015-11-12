package com.frozendroid.beargun.models;

import com.frozendroid.beargun.BearGun;
import com.frozendroid.beargun.MinigameManager;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Queue {

    private Arena arena;
    private List<MinigamePlayer> players = new ArrayList<>();
    private long timeTillStart = 0;
    private Long startingSince;
    private BukkitTask starting_timer;
    private BukkitTask checker_timer;
    private BukkitTask waiting_timer;

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
        arena.setQueue(this);
    }

    public void startWaitingTimer()
    {
        waiting_timer = BearGun.plugin.getServer().getScheduler().runTaskTimer(BearGun.plugin, () -> {
            if (checker_timer == null) {
                startQueueChecker();
            }
            players.forEach((player) -> player.getPlayer().sendMessage("Waiting for "+(arena.getMinPlayers()-players.size())+" more players to join..."));
        }, 0L, 20L*5L);
    }

    public void startQueueChecker()
    {
        checker_timer = BearGun.plugin.getServer().getScheduler().runTaskTimer(BearGun.plugin, () -> {
            if (arena.getMinPlayers() <= players.size()) {
                startingSince = System.currentTimeMillis()/1000L;
                startStartingTimer();
                this.waiting_timer.cancel();
                this.checker_timer.cancel();
            }
        }, 0L, 10L);
    }

    public void startStartingTimer()
    {
        starting_timer = BearGun.plugin.getServer().getScheduler().runTaskTimer(BearGun.plugin, () -> {
            timeTillStart = 5-(System.currentTimeMillis()/1000L-startingSince);
            if (timeTillStart <= 0) {
                Match match = new Match();
                MinigameManager.addMatch(match);
                match.setArena(arena);
                players.forEach(match::addPlayer);
                match.start();
                this.starting_timer.cancel();
            }
            players.forEach((player) -> {
                player.getPlayer().sendMessage("Starting in "+timeTillStart);
            });
        }, 0L, 20L);
    }

    public void addPlayer(MinigamePlayer player)
    {
        MinigameManager.addPlayer(player);
        players.add(player);
    }

    public List<MinigamePlayer> getPlayers() {
        return players;
    }
}
