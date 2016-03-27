package com.frozendroid.beargun;

import com.frozendroid.beargun.commands.CommandHandler;
import com.frozendroid.beargun.configs.ArenaConfig;
import com.frozendroid.beargun.configs.GunConfig;
import com.frozendroid.beargun.listeners.ActionListener;
import com.frozendroid.beargun.listeners.DeathListener;
import com.frozendroid.beargun.listeners.PlayerListener;
import com.frozendroid.beargun.models.Gun;
import com.frozendroid.beargun.utils.ConfigLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BearGun extends JavaPlugin {

    public static Plugin plugin;

    @Override
    public void onEnable()
    {
        plugin = this;

        Bukkit.getScheduler().runTaskLater(this, () -> {
            GunConfig.loadGuns();
            ArenaConfig.loadArenas();
        }, 1L);

        new DeathListener(this);
        new ActionListener(this);
        new PlayerListener(this);
        new CommandHandler(this);
    }

    @Override
    public void onDisable()
    {
        MinigameManager.endAllMatches();
    }

}
