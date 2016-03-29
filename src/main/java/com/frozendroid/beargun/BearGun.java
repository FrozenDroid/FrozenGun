package com.frozendroid.beargun;

import com.frozendroid.beargun.commands.CommandHandler;
import com.frozendroid.beargun.configs.ArenaConfig;
import com.frozendroid.beargun.configs.WeaponConfig;
import com.frozendroid.beargun.listeners.ActionListener;
import com.frozendroid.beargun.listeners.DeathListener;
import com.frozendroid.beargun.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.cyberiantiger.minecraft.unsafe.v1_9_R1.NBTTools;

public class BearGun extends JavaPlugin {

    public static Plugin plugin;
    public static NBTTools nbtTools;

    @Override
    public void onEnable()
    {
        plugin = this;

        Bukkit.getScheduler().runTaskLater(this, () -> {
            WeaponConfig.loadGuns();
            ArenaConfig.loadArenas();

            WeaponManager.getGuns().forEach(gun -> System.out.println("Gun loaded: " + gun.getName() ));

        }, 1L);

        new DeathListener(this);
        new ActionListener(this);
        new PlayerListener(this);
        new CommandHandler(this);


        nbtTools = new NBTTools();
    }

    public static NBTTools getNbtTools()
    {
        return nbtTools;
    }

    @Override
    public void onDisable()
    {
        MinigameManager.endAllMatches();
    }

}
