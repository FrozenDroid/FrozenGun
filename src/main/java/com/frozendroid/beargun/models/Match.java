package com.frozendroid.beargun.models;

import com.frozendroid.beargun.BearGun;
import com.frozendroid.beargun.MinigameManager;
import com.frozendroid.beargun.interfaces.GameObjective;
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
    private List<GameObjective> objectives = new ArrayList<>();

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
        cooldownbars.put(player, Bukkit.getScheduler().runTaskTimer(BearGun.plugin, () -> player.getPlayer().setExp(
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

        objectives.forEach((objective) -> {
            BearGun.plugin.getServer().broadcastMessage(objective.getEndText());
            objective.stop();
            objective.reset();
        });

        Bukkit.getScheduler().runTaskLater(BearGun.plugin, () -> {
             players.forEach((player) -> {
                 this.stopCooldownBar(player);
                 this.stopScoreboard(player);
                 player.leave(this);
             });

             MinigameManager.getMatches().remove(this);
         }, 5L);
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

    public void stopScoreboard(MinigamePlayer player)
    {
        player.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    public void start()
    {
        players.forEach((player) -> {
            player.setLastLocation(player.getPlayer().getLocation());
            player.join(this);
            player.setGun(new Gun(arena.getGun(), player));
            Spawn spawn = getFeasibleSpawn();
            player.getPlayer().teleport(spawn.getLocation());
            startCooldownBar(player);
        });
        startScoreboard();
        this.objectives = arena.getObjectives();
        this.getObjectives().forEach((objective) -> {
            objective.setMatch(this);
            objective.start();
        });
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

    public Arena getArena()
    {
        return arena;
    }

    public void setArena(Arena arena)
    {
        arena.getObjectives().stream().forEach(objective -> {
            try {
                addObjective(objective.getClass().newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        this.arena = arena;
    }

    public void addObjective(GameObjective objective)
    {
        this.objectives.add(objective);
    }

    public List<GameObjective> getObjectives()
    {
        return objectives;
    }

    public Scoreboard getScoreboard()
    {
        return scoreboard;
    }

    public void setObjectives(List<GameObjective> objectives)
    {
        this.objectives = objectives;
    }
}
