package com.frozendroid.frozengun.listeners;

import com.frozendroid.frozengun.MinigameManager;
import com.frozendroid.frozengun.models.MinigamePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class PlayerListener implements Listener {

    private Plugin plugin;

    public PlayerListener(Plugin plugin)
    {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event)
    {
        MinigamePlayer player = MinigameManager.getPlayer(event.getPlayer());

        if (player == null) {
            return;
        }

        // TODO: change this to work with some file to track player's state.
        if (player.isInMatch()) {
            player.getMatch().leave(player);
        }
    }

}
