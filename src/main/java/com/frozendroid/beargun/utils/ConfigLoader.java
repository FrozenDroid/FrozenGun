package com.frozendroid.beargun.utils;

import com.frozendroid.beargun.BearGun;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigLoader {

    public static FileConfiguration getArenaConfig()
    {
        ConfigAccessor configAccessor = new ConfigAccessor((JavaPlugin) BearGun.plugin, "arenas.yml");
        configAccessor.saveDefaultConfig();
        configAccessor.reloadConfig();
        return configAccessor.getConfig();
    }

    public static FileConfiguration getGunConfig()
    {
        ConfigAccessor configAccessor = new ConfigAccessor((JavaPlugin) BearGun.plugin, "guns.yml");
        configAccessor.saveDefaultConfig();
        configAccessor.reloadConfig();
        return configAccessor.getConfig();
    }

}
