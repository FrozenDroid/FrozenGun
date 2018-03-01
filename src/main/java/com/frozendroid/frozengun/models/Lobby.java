package com.frozendroid.frozengun.models;

import com.frozendroid.frozengun.FrozenGun;
import com.frozendroid.frozengun.Messenger;
import com.frozendroid.frozengun.MinigameManager;
import com.frozendroid.frozengun.events.LobbyCountdownEvent;
import com.frozendroid.frozengun.interfaces.Messageable;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Lobby implements Messageable {

    private Arena arena;
    private Location location;
    private List<MinigamePlayer> players = new ArrayList<>();
    private long countdownTime = 20;
    private long timeTillStart = 20;
    private long startingSince;

    private BukkitTask starting_timer;
    private BukkitTask checker_timer;
    private BukkitTask waiting_timer;

    public Lobby(Arena arena) {
        this.arena = arena;
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public long getTimeTillStart() {
        return timeTillStart;
    }

    public void setTimeTillStart(Integer timeTillStart) {
        this.timeTillStart = timeTillStart;
    }

    public boolean isWaiting() {
        return waiting_timer != null;
    }

    public void startWaitingTimerIfNotStarted() {
        if (!this.isWaiting() && this.starting_timer == null) {
            this.timeTillStart = this.countdownTime;
            this.startWaitingTimer();
        }
    }

    public void startWaitingTimer() {
        this.timeTillStart = this.countdownTime;
        waiting_timer = FrozenGun.plugin.getServer().getScheduler().runTaskTimer(FrozenGun.plugin, () -> {
            if (checker_timer == null) {
                startLobbyChecker();
            }
            int delta = arena.getMinPlayers() - players.size();
            if (delta != 0)
                players.forEach((player) -> player.getPlayer().sendMessage(Messenger.infoMsg("Waiting for " + delta + " more players to join...")));
        }, 0L, 20L * 5L);
    }

    public void stop() {
        if (waiting_timer != null) {
            waiting_timer.cancel();
            this.waiting_timer = null;
        }

        if (checker_timer != null) {
            checker_timer.cancel();
            this.checker_timer = null;
        }

        if (starting_timer != null) {
            starting_timer.cancel();
            this.starting_timer = null;
        }
    }

    public void reset() {
        this.stop();
        this.players = new ArrayList<>();
    }

    public void startLobbyChecker() {
        checker_timer = FrozenGun.plugin.getServer().getScheduler().runTaskTimer(FrozenGun.plugin, () -> {
            if (arena.getMinPlayers() <= players.size()) {
                startingSince = System.currentTimeMillis() / 1000L;
                startStartingTimer();
                if (this.waiting_timer != null)
                    this.waiting_timer.cancel();
                this.waiting_timer = null;
                if (this.checker_timer != null)
                    this.checker_timer.cancel();
                this.checker_timer = null;
            }
        }, 0L, 10L);
    }

    public void startStartingTimer() {
        starting_timer = FrozenGun.plugin.getServer().getScheduler().runTaskTimer(FrozenGun.plugin, () -> {
            // If the amount of players gets below the minimum amount needed, start waiting again
            if (players.size() < arena.getMinPlayers()) {
                this.timeTillStart = countdownTime;
                startWaitingTimer();
                starting_timer.cancel();
                starting_timer = null;
                return;
            }
            FrozenGun.plugin.getServer().getPluginManager().callEvent(
                    new LobbyCountdownEvent(this, this)
            );
            players.forEach((player) -> {
                if (timeTillStart > 0) {
                    if (timeTillStart <= 5) {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, 1f);
                    }
                }
            });
            if (timeTillStart <= 0) {
                Match match = new Match();
                MinigameManager.addMatch(match);
                match.setArena(arena);
                players.forEach(match::addPlayer);
                match.start();
                players.forEach((player) -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 2F, 2F));
                if (this.starting_timer != null) {
                    this.starting_timer.cancel();
                }
            }
            timeTillStart--;
        }, 0L, 20L);
    }

    public void addPlayer(MinigamePlayer player) {
        players.add(player);
    }

    public void removePlayer(MinigamePlayer player) {
        MinigameManager.removePlayer(player);
        players.remove(player);
    }

    public List<MinigamePlayer> getPlayers() {
        return players;
    }

    public Optional<Location> getLocation() {
        return Optional.ofNullable(location);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getCountdownTime() {
        return countdownTime;
    }

    public void setCountdownTime(long countdownTime) {
        this.countdownTime = countdownTime;
    }

    @Override
    public void sendMessage(String s) {
        players.forEach(p -> p.sendMessage(s));
    }

}
