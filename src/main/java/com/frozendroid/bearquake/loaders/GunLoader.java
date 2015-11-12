package com.frozendroid.bearquake.loaders;

import com.frozendroid.bearquake.MinigameManager;
import com.frozendroid.bearquake.models.Gun;
import com.frozendroid.bearquake.utils.ConfigLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class GunLoader {

    public static boolean loadGuns()
    {
        FileConfiguration config = ConfigLoader.getGunConfig();

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
