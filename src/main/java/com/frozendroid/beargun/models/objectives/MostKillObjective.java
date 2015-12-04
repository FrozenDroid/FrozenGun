package com.frozendroid.beargun.models.objectives;

import com.frozendroid.beargun.BearGun;
import com.frozendroid.beargun.events.PlayerShotEvent;
import com.frozendroid.beargun.interfaces.GameObjective;
import com.frozendroid.beargun.models.Kill;
import com.frozendroid.beargun.models.Match;
import com.frozendroid.beargun.models.MinigamePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Stream;

public class MostKillObjective implements GameObjective, Listener {

    private Match match;
    private Integer killGoal;
    private HashMap<MinigamePlayer, ArrayList<Kill>> kills = new HashMap<>();

    @EventHandler
    public void onPlayerShot(PlayerShotEvent event)
    {
        if (event.getVictim().getHealth()-event.getGun().getDamage() > 0)
            return;
        Kill kill = new Kill();
        kill.setKilled(event.getVictim());
        kill.setKiller(event.getShooter());
        kill.setTime(System.currentTimeMillis());
        kills.putIfAbsent(event.getShooter(), new ArrayList<>());
        ArrayList<Kill> _kills = kills.get(event.getShooter());
        if (_kills.size() >= 1) {
            Kill _kill = _kills.get(_kills.size()-1);
            if (_kill != null) {

                System.out.println(_kill.getTime());
            }
        }
        if (_kills.size() >= 2) {
            Kill lastKill = _kills.get(_kills.size()-1);
            Kill currentKill = kill;
            Long delta = currentKill.getTime() - lastKill.getTime();

            if (match.getArena().getKillingSpreeDelay() >= delta) {
                kill.setSpree(lastKill.getSpree()+1);
            }
        }
        _kills.add(kill);
        for (MinigamePlayer player : kills.keySet()) {
            if (kills.get(player).size() >= killGoal) {
                match.end();
                return;
            }
        }
    }

    @Override
    public void setMatch(Match match)
    {
        this.match = match;
    }

    @Override
    public Match getMatch()
    {
        return match;
    }

    @Override
    public String getEndText()
    {
        Comparator<MinigamePlayer> byKills = (p1, p2) -> Integer.compare(kills.get(p2).size(), kills.get(p1).size());
        Stream<MinigamePlayer> stream =  kills.keySet().stream().sorted(byKills);
        return stream.findFirst().get().getPlayer().getName() + " won the game at "+match.getArena().getName();
    }

    @Override
    public String getTypeName()
    {
        return "most_kills";
    }

    @Override
    public Object getGoal()
    {
        return killGoal;
    }

    @Override
    public void setGoal(Integer i)
    {
        this.killGoal = i;
    }

    @Override
    public void start()
    {
        BearGun.plugin.getServer().getPluginManager().registerEvents(this, BearGun.plugin);
    }

    @Override
    public void stop()
    {
        PlayerShotEvent.getHandlerList().unregister(this);
    }

    @Override
    public void reset()
    {
        kills.clear();
    }
}
