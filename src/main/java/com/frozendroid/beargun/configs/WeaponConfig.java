package com.frozendroid.beargun.configs;

import com.frozendroid.beargun.BearGun;
import com.frozendroid.beargun.MinigameManager;
import com.frozendroid.beargun.models.Gun_old;
import com.frozendroid.beargun.models.Railgun;
import com.frozendroid.beargun.utils.ConfigLoader;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class WeaponConfig {

    private static FileConfiguration config;

    public static FileConfiguration get()
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
            switch (section.getString("type")) {
                case "RAILGUN":
                    loadRailgun(section);
                    break;
            }
        });

        return true;
    }

    public static void loadRailgun(ConfigurationSection section)
    {
        Railgun gun = new Railgun();
        gun.setMaterial(Material.getMaterial(section.getString("material")));
        gun.setName(section.getString("display_name"));
        gun.setRange(section.getDouble("range"));
        gun.setDamage(section.getDouble("damage"));
        gun.setCooldown(section.getDouble("cooldown"));

        List<?> pass_through = section.getList("pass_through");
        if (pass_through != null) {
            List<String> pass_through_strings = (List<String>) pass_through;
            pass_through_strings.forEach((string) -> {
                Material material = null;
                try {
                    material = Material.valueOf(string);
                } catch (IllegalArgumentException e) {
                    BearGun.plugin.getLogger().warning("The material \"" + string + "\" was not found...");
                }
                if (material != null)
                    gun.addPassthroughMaterial(material);
            });
        }

        MinigameManager.addWeapon(gun);
    }

    public static void save()
    {
        ConfigLoader.saveGunConfig();
    }

}
