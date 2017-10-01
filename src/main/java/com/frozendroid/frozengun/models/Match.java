package com.frozendroid.frozengun.models;

import com.frozendroid.frozengun.FrozenGun;
import com.frozendroid.frozengun.Messenger;
import com.frozendroid.frozengun.MinigameManager;
import com.frozendroid.frozengun.models.objectives.GameObjective;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class Match {

    private boolean ended = false;
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
        players.forEach(player -> player.sendMessage(Messenger.infoMsg(string)));
    }

    public void startCooldownBar(MinigamePlayer player)
    {
        cooldownbars.put(player, Bukkit.getScheduler().runTaskTimer(FrozenGun.plugin, () -> {
            if (player.getWeaponInHand() instanceof Gun) {
                Gun gun = (Gun) player.getWeaponInHand();
                player.setExp(1F / ((float) gun.getCooldown()) * (float) gun.getCooldownTime() / 1000);
            } else {
                player.setExp(0);
            }
        }, 0L, 1L));
    }

    public void stopCooldownBar(MinigamePlayer player)
    {
        cooldownbars.forEach((player_, task) -> task.cancel());
        cooldownbars.remove(cooldownbars.keySet().stream().filter(player_ -> player_.equals(player)).findFirst().orElse(null));
    }

    public void end()
    {
        if (ended)
            return;
        setEnded(true);
        objectives.forEach((objective) -> {
            FrozenGun.plugin.getServer().broadcastMessage(Messenger.infoMsg(objective.getEndText()));
            objective.stop();
            objective.reset();
        });

        players.forEach(player -> {
            player.sendTitle(ChatColor.BOLD + "" + ChatColor.BLUE + "Victory", "You won!");
            if (player.getWeaponInHand() instanceof Gun) {
                Gun gun = (Gun) player.getWeaponInHand();
                gun.setCooldown(10);
                gun.lastShot = System.currentTimeMillis();
            }
        });
        Bukkit.getServer().getScheduler().runTaskLater(FrozenGun.plugin, () -> {
            Set<MinigamePlayer> playerSet = new HashSet<>();
            playerSet.addAll(players);
            playerSet.forEach(player -> {
                this.stopCooldownBar(player);
                this.stopScoreboard(player);
                leave(player);
            });
            arena.setOccupied(false);
            MinigameManager.getMatches().remove(this);
        }, 20L*10);
    }

    public void start()
    {
        arena.setQueue(null);
        arena.setOccupied(true);
        players.forEach((player) -> {
            player.setQueue(null);
            player.setLastFoodLevel(player.getFoodLevel());
            player.setLastHealth(player.getHealth());
            player.setLastMaxHealth(player.getMaxHealth());
            player.setLastExp(player.getExp());
            player.setLastLocation(player.getLocation());
            player.setLastInventoryContents(player.getInventory().getContents());
            player.setLastGamemode(player.getGameMode());
            player.join(this);
            player.resetMaxHealth();
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setWalkSpeed(arena.getRunSpeed());
            player.getInventory().setHeldItemSlot(0);
            player.setGameMode(GameMode.SURVIVAL);

            player.getInventory().clear();

            for (Weapon weapon : arena.getWeapons()) {
                Weapon _weapon = weapon.clone();
                _weapon.setPlayer(player);
                player.addWeapon(_weapon);
            }

            Spawn spawn = getFeasibleSpawn();
            player.teleport(spawn.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            startCooldownBar(player);
        });
        startScoreboard();
        this.objectives = arena.getObjectives();
        this.getObjectives().forEach((objective) -> {
            objective.setMatch(this);
            objective.start();
        });
    }

    public void leave(MinigamePlayer player)
    {
        stopScoreboard(player);
        player.setFoodLevel(player.getLastFoodLevel());
        player.setMaxHealth(player.getLastMaxHealth());
        player.setHealth(player.getLastHealth());
        player.getInventory().setContents(player.getLastInventoryContents());
        player.setExp(player.getLastExp());
        player.teleport(player.getLastLocation());
        player.setGameMode(player.getLastGamemode());
        player.setWalkSpeed(0.2f);
        MinigameManager.removePlayer(player);
        objectives.forEach((objective) -> objective.removePlayer(player));
        players.remove(player);

        if (players.size() <= 1 && !this.ended)
            this.end();
    }

    public void startScoreboard()
    {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Team team = scoreboard.registerNewTeam("all");
        team.setDisplayName("test");
        Objective objective = scoreboard.registerNewObjective("test", "playerKillCount");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("Kills");
        players.forEach((player) -> {
            team.addPlayer(player.getPlayer());
            player.getPlayer().setScoreboard(scoreboard);
            objective.getScore(player.getPlayer()).setScore(0);
        });
    }

    public void stopScoreboard(MinigamePlayer player)
    {
        scoreboard.resetScores(player.getPlayer());
        player.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    public Spawn getFeasibleSpawn()
    {
        return arena.getSpawns().stream().filter(Spawn::isFeasible).findFirst().orElse(arena.getSpawns().stream().findAny().get());
    }

    public Arena getArena()
    {
        return arena;
    }

    public void setArena(Arena arena)
    {
        objectives.clear();
        this.arena = arena;
    }

    public void removePlayer(MinigamePlayer player)
    {
        players.remove(player);
        if (players.size() == 0) {
            end();
        }
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

    public boolean isEnded()
    {
        return ended;
    }

    public void setEnded(boolean ended)
    {
        this.ended = ended;
    }
}
