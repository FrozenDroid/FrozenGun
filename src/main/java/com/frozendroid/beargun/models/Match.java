package com.frozendroid.beargun.models;

import com.frozendroid.beargun.BearGun;
import com.frozendroid.beargun.Messenger;
import com.frozendroid.beargun.MinigameManager;
import com.frozendroid.beargun.interfaces.GameObjective;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Objective;
import org.cyberiantiger.minecraft.nbt.CompoundTag;
import org.cyberiantiger.minecraft.nbt.DoubleTag;
import org.cyberiantiger.minecraft.nbt.ListTag;
import org.cyberiantiger.minecraft.nbt.TagType;

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
        players.stream().forEach((player) -> player.sendMessage(Messenger.infoMsg(string)));
    }

    public void startCooldownBar(MinigamePlayer player)
    {
        cooldownbars.put(player, Bukkit.getScheduler().runTaskTimer(BearGun.plugin, () -> {
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
            BearGun.plugin.getServer().broadcastMessage(Messenger.infoMsg(objective.getEndText()));
            objective.stop();
            objective.reset();
        });

        Set<MinigamePlayer> playerSet = new HashSet<>();
        playerSet.addAll(players);
        playerSet.forEach(player -> {
            this.stopCooldownBar(player);
            this.stopScoreboard(player);
            leave(player, false);
        });
        arena.setOccupied(false);
        MinigameManager.getMatches().remove(this);
    }

    public void start()
    {
        arena.setOccupied(true);
        players.forEach((player) -> {
            player.setLastFoodLevel(player.getFoodLevel());
            player.setLastHealth(player.getHealth());
            player.setLastMaxHealth(player.getMaxHealth());
            player.setLastExp(player.getExp());
            player.setLastLocation(player.getLocation());
            player.setLastInventoryContents(player.getInventory().getContents());
            player.setLastGamemode(player.getGameMode());
            player.join(this);
            player.setGameMode(GameMode.SURVIVAL);

            for (Weapon weapon : arena.getWeapons()) {
                Weapon _weapon = weapon.clone();
                _weapon.setPlayer(player);
                player.addWeapon(_weapon);
            }

            Spawn spawn = getFeasibleSpawn();
            player.teleport(spawn.getLocation());
            startCooldownBar(player);
        });
        startScoreboard();
        this.objectives = arena.getObjectives();
        this.getObjectives().forEach((objective) -> {
            objective.setMatch(this);
            objective.start();
        });
    }

    public void leave(MinigamePlayer player, boolean useNbt)
    {
        stopScoreboard(player);
        if (!useNbt) {
            player.setFoodLevel(player.getLastFoodLevel());
            player.setMaxHealth(player.getLastMaxHealth());
            player.setHealth(player.getLastHealth());
            player.getInventory().setContents(player.getLastInventoryContents());
            player.setExp(player.getLastExp());
            player.teleport(player.getLastLocation());
            player.setGameMode(player.getLastGamemode());
        } else {
            Player entity = (Player) BearGun.getNbtTools().getEntityByUUID(player.getWorld(), player.getUniqueId());
            CompoundTag tag = BearGun.getNbtTools().readEntity(entity);
            DoubleTag[] tags = new DoubleTag[3];
            tags[0] = new DoubleTag(player.getLastLocation().getBlockX());
            tags[1] = new DoubleTag(player.getLastLocation().getBlockY());
            tags[2] = new DoubleTag(player.getLastLocation().getBlockZ());
            ListTag listTag = new ListTag(TagType.DOUBLE, tags);
            tag.setList("Pos", listTag);
            entity.teleport(player.getLastLocation());
            entity.setFoodLevel(player.getLastFoodLevel());
            entity.setMaxHealth(player.getLastMaxHealth());
            entity.setHealth(player.getLastHealth());
            entity.getInventory().setContents(player.getLastInventoryContents());
            entity.setExp(player.getLastExp());
            entity.setGameMode(player.getLastGamemode());
            BearGun.getNbtTools().updateEntity(entity, tag);
        }

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
        players.stream().forEach((player) -> {
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
