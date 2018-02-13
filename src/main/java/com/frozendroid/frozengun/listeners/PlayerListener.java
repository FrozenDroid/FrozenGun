package com.frozendroid.frozengun.listeners;

import com.frozendroid.frozengun.MinigameManager;
import com.frozendroid.frozengun.models.MinigamePlayer;
import com.xxmicloxx.NoteBlockAPI.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
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

        if (player.getLobby() != null) {
            player.getLobby().removePlayer(player);
        }

        // TODO: change this to work with some file to track player's state.
        if (player.isInMatch()) {
            player.getMatch().leave(player);
        }
    }

    @EventHandler
    public void onPlayerHungry(FoodLevelChangeEvent event) {
        /*
         * Player is the only subclass of HumanEntity but this might change in the future - that's why I
         * have this check in place.
         */
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            MinigamePlayer minigamePlayer = MinigameManager.getPlayer(player);
            if (minigamePlayer == null || !minigamePlayer.isInMatch()) {
                return;
            }
            event.setCancelled(true);
        }
    }

}
