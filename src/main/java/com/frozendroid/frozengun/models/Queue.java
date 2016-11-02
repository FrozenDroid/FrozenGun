package com.frozendroid.frozengun.models;

import com.frozendroid.frozengun.FrozenGun;
import com.frozendroid.frozengun.Messenger;
import com.frozendroid.frozengun.MinigameManager;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Queue {

    private Arena arena;
    private List<MinigamePlayer> players = new ArrayList<>();
    private long timeTillStart = 20;
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
        timeTillStart = arena.getStartingTime();
    }

    public void setTimeTillStart(Integer timeTillStart)
    {
        this.timeTillStart = timeTillStart;
    }

    public long getTimeTillStart()
    {
        return timeTillStart;
    }

    public void startWaitingTimer()
    {
        waiting_timer = FrozenGun.plugin.getServer().getScheduler().runTaskTimer(FrozenGun.plugin, () -> {
            if (checker_timer == null) {
                startQueueChecker();
            }
            int delta = arena.getMinPlayers()-players.size();
            if (delta != 0)
                players.forEach((player) -> player.getPlayer().sendMessage(Messenger.infoMsg("Waiting for "+delta+" more players to join...")));
        }, 0L, 20L*5L);
    }

    public void stop()
    {
        if (waiting_timer != null)
            waiting_timer.cancel();

        if (checker_timer != null)
            checker_timer.cancel();

        if (starting_timer != null)
            starting_timer.cancel();
    }

    public void startQueueChecker()
    {
        checker_timer = FrozenGun.plugin.getServer().getScheduler().runTaskTimer(FrozenGun.plugin, () -> {
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
        starting_timer = FrozenGun.plugin.getServer().getScheduler().runTaskTimer(FrozenGun.plugin, () -> {


            players.forEach((player) -> {
                if (timeTillStart % 5 == 0 && timeTillStart > 0) {
                    player.getPlayer().sendMessage(Messenger.infoMsg("Starting in " + timeTillStart));
                } else if (timeTillStart <= 5 && timeTillStart > 0) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
                    player.getPlayer().sendMessage(Messenger.infoMsg("Starting in " + timeTillStart));
                }
            });
            if (timeTillStart <= 0) {
                Match match = new Match();
                MinigameManager.addMatch(match);
                match.setArena(arena);
                players.forEach(match::addPlayer);
                match.start();
                arena.setQueue(null);
                players.forEach((player) -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 2F, 2F));
                this.starting_timer.cancel();
            }
            timeTillStart--;
        }, 0L, 20L);
    }

    public void addPlayer(MinigamePlayer player)
    {
        player.sendMessage(Messenger.infoMsg("Joined the queue for " + arena.getName()) + ".");
        MinigameManager.addPlayer(player);
        players.add(player);
    }

    public void removePlayer(MinigamePlayer player)
    {
        MinigameManager.removePlayer(player);
        players.remove(player);
    }

    public List<MinigamePlayer> getPlayers() {
        return players;
    }
}
