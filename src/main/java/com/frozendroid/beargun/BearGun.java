package com.frozendroid.beargun;

import com.frozendroid.beargun.commands.CommandHandler;
import com.frozendroid.beargun.listeners.ActionListener;
import com.frozendroid.beargun.listeners.DeathListener;
import com.frozendroid.beargun.loaders.ArenaLoader;
import com.frozendroid.beargun.loaders.GunLoader;
import com.frozendroid.beargun.models.Match;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BearGun extends JavaPlugin {

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
