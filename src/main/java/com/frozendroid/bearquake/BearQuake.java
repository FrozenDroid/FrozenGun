package com.frozendroid.bearquake;

import com.frozendroid.bearquake.commands.CommandHandler;
import com.frozendroid.bearquake.listeners.ActionListener;
import com.frozendroid.bearquake.listeners.DeathListener;
import com.frozendroid.bearquake.loaders.ArenaLoader;
import com.frozendroid.bearquake.loaders.GunLoader;
import com.frozendroid.bearquake.models.Match;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BearQuake extends JavaPlugin {

    public static Plugin plugin;

    @Override
    public void onEnable()
    {
        plugin = this;

        GunLoader.loadGuns();
        ArenaLoader.loadArenas();

        new DeathListener(this);
        new ActionListener(this);
        new CommandHandler(this);

        Match match = new Match();
        MinigameManager.addMatch(match);
    }

    @Override
    public void onDisable()
    {

    }

}
