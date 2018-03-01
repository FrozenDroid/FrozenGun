package com.frozendroid.frozengun.models.objectives;

import com.frozendroid.frozengun.FrozenGun;
import com.frozendroid.frozengun.events.PlayerKillEvent;
import com.frozendroid.frozengun.events.PlayerShotEvent;
import com.frozendroid.frozengun.events.multikills.*;
import com.frozendroid.frozengun.models.Kill;
import com.frozendroid.frozengun.models.Match;
import com.frozendroid.frozengun.models.MinigamePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scoreboard.Score;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GameObjective implements Listener {

    public Match match;
    public HashMap<MinigamePlayer, ArrayList<Kill>> kills = new HashMap<>();
    public HashMap<MinigamePlayer, Integer> deaths = new HashMap<>();
    boolean achieved = false;
    private ConfigurationSection section;

    public abstract Match getMatch();

    public abstract void setMatch(Match match);

    public abstract String getEndText();

    public abstract String getTypeName();

    public abstract Object getGoal();

    public abstract void setGoal(Integer i);

    public abstract void removePlayer(MinigamePlayer player);

    public abstract void start();

    public abstract void stop();

    public abstract ArrayList<MinigamePlayer> getWinners();

    public void reset() {
        kills.clear();
    }

    public void onPlayerShot(PlayerShotEvent event) {
        if (event.getVictim().getHealth() - event.getGun().getDamage() > 0)
            return;

        Score score = match.getScoreboard().getObjective("killObjective").getScore(event.getShooter().getDisplayName());
        score.setScore(score.getScore() + 1);

        PluginManager pluginManager = FrozenGun.plugin.getServer().getPluginManager();
        pluginManager.callEvent(new PlayerKillEvent(match, event.getShooter(), event.getVictim()));

        Kill kill = new Kill();
        kill.setKilled(event.getVictim());
        kill.setKiller(event.getShooter());
        kill.setTime(System.currentTimeMillis());
        kills.putIfAbsent(event.getShooter(), new ArrayList<>());

        deaths.put(event.getVictim(), deaths.getOrDefault(event.getVictim(), 0) + 1);

        ArrayList<Kill> _kills = kills.get(event.getShooter());

        if (_kills.size() >= 1) {
            Kill lastKill = _kills.get(_kills.size() - 1);

            Long delta = kill.getTime() - lastKill.getTime();
            if (match.getArena().getKillingSpreeDelay() >= delta / 1000) {
                kill.setSpree(lastKill.getSpree() + 1);

                if (kill.getSpree() == 2)
                    pluginManager.callEvent(new DoubleKillEvent(match, event.getShooter()));
                if (kill.getSpree() == 3)
                    pluginManager.callEvent(new TripleKillEvent(match, event.getShooter()));
                if (kill.getSpree() == 4)
                    pluginManager.callEvent(new QuadraKillEvent(match, event.getShooter()));
                if (kill.getSpree() == 5)
                    pluginManager.callEvent(new PentaKillEvent(match, event.getShooter()));
                if (kill.getSpree() == 6)
                    pluginManager.callEvent(new HexaKillEvent(match, event.getShooter()));
            }
        } else
            kill.setSpree(1);

        _kills.add(kill);
    }

    public ConfigurationSection getSection() {
        return section;
    }

    public void setSection(ConfigurationSection section) {
        this.section = section;
    }

}
