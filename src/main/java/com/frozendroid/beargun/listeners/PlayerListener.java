package com.frozendroid.beargun.listeners;

import com.frozendroid.beargun.MinigameManager;
import com.frozendroid.beargun.models.MinigamePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class PlayerListener implements Listener {

    private Plugin plugin;

    public PlayerListener(Plugin plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event)
    {
        MinigamePlayer player = MinigameManager.getPlayer(event.getPlayer());

        if (player == null)
            return;

        if (player.isInMatch())
            player.getMatch().leave(player);
    }

}
