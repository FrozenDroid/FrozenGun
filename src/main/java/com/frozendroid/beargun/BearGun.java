package com.frozendroid.beargun;

import com.frozendroid.beargun.commands.CommandHandler;
import com.frozendroid.beargun.configs.ArenaConfig;
import com.frozendroid.beargun.configs.WeaponConfig;
import com.frozendroid.beargun.listeners.ActionListener;
import com.frozendroid.beargun.listeners.DeathListener;
import com.frozendroid.beargun.listeners.PlayerListener;
import com.frozendroid.beargun.models.Arena;
import com.frozendroid.beargun.models.MinigamePlayer;
import com.frozendroid.beargun.models.Weapon;
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

            for (Arena arena : MinigameManager.getArenas()) {
                getLogger().info("Arena loaded: " + arena.getName());
            }

            for (Weapon weapon : WeaponManager.getWeapons()) {
                getLogger().info("Weapon loaded: " + weapon.getName());
            }

        }, 1L);

        new DeathListener(this);
        new ActionListener(this);
        new PlayerListener(this);
        new CommandHandler(this);


        nbtTools = new NBTTools();
    }

    public static void info(String msg)
    {
        plugin.getLogger().info(msg);
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
