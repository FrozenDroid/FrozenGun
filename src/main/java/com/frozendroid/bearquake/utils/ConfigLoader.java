package com.frozendroid.bearquake.utils;

import com.frozendroid.bearquake.BearQuake;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigLoader {

    public static FileConfiguration getArenaConfig()
    {
        ConfigAccessor configAccessor = new ConfigAccessor((JavaPlugin) BearQuake.plugin, "arenas.yml");
        configAccessor.saveDefaultConfig();
        configAccessor.reloadConfig();
        return configAccessor.getConfig();
    }

    public static FileConfiguration getGunConfig()
    {
        ConfigAccessor configAccessor = new ConfigAccessor((JavaPlugin) BearQuake.plugin, "guns.yml");
        configAccessor.saveDefaultConfig();
        configAccessor.reloadConfig();
        return configAccessor.getConfig();
    }

}
