package com.frozendroid.frozengun.utils;

import com.frozendroid.frozengun.FrozenGun;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigLoader {

    private static ConfigAccessor arenaConfig;
    private static ConfigAccessor gunConfig;

    public static FileConfiguration getArenaConfig() {
        arenaConfig = new ConfigAccessor((JavaPlugin) FrozenGun.plugin, "arenas.yml");
        arenaConfig.saveDefaultConfig();
        arenaConfig.reloadConfig();
        return arenaConfig.getConfig();
    }

    public static void saveArenaConfig() {
        arenaConfig.saveConfig();
    }

    public static void saveGunConfig() {
        gunConfig.saveConfig();
    }

    public static FileConfiguration getGunConfig() {
        gunConfig = new ConfigAccessor((JavaPlugin) FrozenGun.plugin, "weapons.yml");
        gunConfig.saveDefaultConfig();
        gunConfig.reloadConfig();
        return gunConfig.getConfig();
    }

}
