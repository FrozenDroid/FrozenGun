package com.frozendroid.bearquake.models;

import com.frozendroid.bearquake.BearQuake;
import com.frozendroid.bearquake.MinigameManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Objective;

import java.util.*;

public class Match {

    private Arena arena;
    private List<MinigamePlayer> players = new ArrayList<>();
    private HashMap<MinigamePlayer, BukkitTask> cooldownbars = new HashMap<>();
    private Scoreboard scoreboard;
    private List<GameObjective> objectives;

    public List<MinigamePlayer> getPlayers()
    {
        return players;
    }

    public void addPlayer(MinigamePlayer player)
    {
        players.add(player);
    }

    public MinigamePlayer findPlayer(UUID uuid)
    {
        return players.stream().filter(matchplayer -> matchplayer.getPlayer().getUniqueId().equals(uuid)).findFirst().
                orElse(null);
    }

    public void broadcast(String string)
    {
        players.stream().forEach((player) -> player.getPlayer().sendMessage(string));
    }

    public void startCooldownBar(MinigamePlayer player)
    {
        cooldownbars.put(player, Bukkit.getScheduler().runTaskTimer(BearQuake.plugin, () -> player.getPlayer().setExp(
                        1F / ((float) player.getGun().getCooldown()) * (float) player.getGun().getCooldownTime() / 1000
                ), 0L, 1L)
        );
    }

    public void stopCooldownBar(MinigamePlayer player)
    {
        cooldownbars.forEach((player_, task) -> task.cancel());
        cooldownbars.remove(cooldownbars.keySet().stream().filter(player_ -> player_.equals(player)).findFirst().orElse(null));
    }

    public void end()
    {
        players.forEach(this::stopCooldownBar);
        MinigameManager.getMatches().remove(this);
    }

    public void startScoreboard()
    {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Team team = scoreboard.registerNewTeam("all");
        team.setDisplayName("test");
        Objective objective = scoreboard.registerNewObjective("test", "playerKillCount");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("Kills");
        players.stream().forEach((player) -> {
            team.addPlayer(player.getPlayer());
            player.getPlayer().setScoreboard(scoreboard);
            objective.getScore(player.getPlayer()).setScore(0);
        });

    }

    public void start()
    {
        players.forEach((player) -> {
            player.join(this);
            player.setGun(new Gun(arena.getGun(), player));
            arena.getSpawns().stream().forEach((spawn_) -> {
                Bukkit.broadcastMessage(spawn_.getLocation().toString() + " feasible? " + spawn_.isFeasible());
            });
            Spawn spawn = getFeasibleSpawn();
            player.getPlayer().teleport(spawn.getLocation());
            startCooldownBar(player);
        });
        startScoreboard();

    }

    public Spawn getFeasibleSpawn()
    {
        return arena.getSpawns().stream().filter(Spawn::isFeasible).findFirst().orElse(arena.getSpawns().stream().findAny().get());
    }

    public void respawn(MinigamePlayer player)
    {
        arena.getSpawns().forEach((spawn) -> {

        });
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        arena.getObjectives().stream().forEach(objective -> {
            try {
                addObjective(objective.getClass().newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        this.arena = arena;
    }

    public void addObjective(GameObjective objective) {
        this.objectives.add(objective);
    }

    public List<GameObjective> getObjectives() {
        return objectives;
    }

    public void setObjectives(List<GameObjective> objectives) {
        this.objectives = objectives;
    }
}
