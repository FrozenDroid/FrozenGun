package com.frozendroid.frozengun.listeners;

import com.frozendroid.frozengun.MinigameManager;
import com.frozendroid.frozengun.models.Match;
import com.frozendroid.frozengun.models.MinigamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

public class DeathListener implements Listener {

    public DeathListener(Plugin plugin)
    {
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

        Match match = player.getMatch();
        event.setDeathMessage(null);
        player.respawn(match);
        event.setKeepInventory(true);
    }

}
