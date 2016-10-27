package com.frozendroid.frozengun;

import com.frozendroid.frozengun.commands.CommandHandler;
import com.frozendroid.frozengun.configs.ArenaConfig;
import com.frozendroid.frozengun.configs.WeaponConfig;
import com.frozendroid.frozengun.listeners.ActionListener;
import com.frozendroid.frozengun.listeners.DeathListener;
import com.frozendroid.frozengun.listeners.PlayerListener;
import com.frozendroid.frozengun.models.Arena;
import com.frozendroid.frozengun.models.Weapon;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.cyberiantiger.minecraft.unsafe.v1_9_R1.NBTTools;

public class FrozenGun extends JavaPlugin {

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
