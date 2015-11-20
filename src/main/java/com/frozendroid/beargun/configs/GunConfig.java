package com.frozendroid.beargun.configs;

import com.frozendroid.beargun.MinigameManager;
import com.frozendroid.beargun.models.Gun;
import com.frozendroid.beargun.utils.ConfigLoader;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class GunConfig {

    private static FileConfiguration config;

    private static FileConfiguration get()
    {
        return config;
    }

    public static boolean loadGuns()
    {
        config = ConfigLoader.getGunConfig();

        List<ConfigurationSection> gunsections = new ArrayList<>();

        config.getConfigurationSection("guns").getKeys(false).forEach((String key) -> {
            gunsections.add(config.getConfigurationSection("guns."+key));
        });

        gunsections.forEach((ConfigurationSection section) -> {
            Gun gun = new Gun();
            gun.setMaterial(Material.getMaterial(section.getString("material")));
            gun.setName(section.getString("display_name"));
            gun.setDamage(section.getDouble("damage"));
            gun.setCooldown(section.getDouble("cooldown"));
            MinigameManager.addGun(gun);
        });

        return true;
    }

}
