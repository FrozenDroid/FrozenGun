package com.frozendroid.beargun.listeners;

import com.frozendroid.beargun.MinigameManager;
import com.frozendroid.beargun.models.Match;
import com.frozendroid.beargun.models.MinigamePlayer;
import com.frozendroid.beargun.models.Spawn;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

public class DeathListener implements Listener {

    private Plugin plugin;

    public DeathListener(Plugin plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        MinigamePlayer player = MinigameManager.getPlayer(event.getEntity());

        if (player == null)
            return;

        if (!player.isInMatch())
            return;

        event.getDrops().clear();
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        MinigamePlayer player = MinigameManager.getPlayer(event.getPlayer());

        if (player == null)
            return;

        if (!player.isInMatch())
            return;


        Match match = player.getMatch();
        Spawn spawn = match.getFeasibleSpawn();
        event.setRespawnLocation(spawn.getLocation());
        player.respawn(match);

    }

}
