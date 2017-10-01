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

import java.util.Comparator;
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
        Comparator<MinigamePlayer> byKills = (p1, p2) -> Integer.compare(kills.get(p2).size(), kills.get(p1).size());
        Stream<MinigamePlayer> stream =  kills.keySet().stream().sorted(byKills);
        MinigamePlayer player = stream.findFirst().orElse(null);
        if (player == null) {
            return "The game at " + match.getArena().getName() + " ended.";
        }
        for (int i = 0; i < 20; i++) {
            Bukkit.getServer().getScheduler().runTaskLater(FrozenGun.plugin, () -> {
                Firework fw = player.getWorld().spawn(player.getLocation(), Firework.class);
                FireworkMeta meta = fw.getFireworkMeta();
                FireworkEffect fe = FireworkEffect.builder().withColor(Color.BLUE).trail(true).flicker(true).with(FireworkEffect.Type.BALL_LARGE).withFlicker().withColor(Color.BLUE).withFade(Color.RED).build();
                meta.setPower(1);
                meta.addEffect(fe);
                fw.setFireworkMeta(meta);
            }, 5L*i);
        }
        return player.getPlayer().getName() + " won the game at " + match.getArena().getName() + "!";
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
}
