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
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

}
