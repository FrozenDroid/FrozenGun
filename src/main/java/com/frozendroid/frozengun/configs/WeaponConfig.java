package com.frozendroid.frozengun.configs;

import com.frozendroid.frozengun.FrozenGun;
import com.frozendroid.frozengun.WeaponManager;
import com.frozendroid.frozengun.models.Grenade;
import com.frozendroid.frozengun.models.Railgun;
import com.frozendroid.frozengun.utils.ConfigLoader;
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
                case "GRENADE":
                    loadGrenade(section);
                    break;
            }
        });

        return true;
    }

    public static void loadGrenade(ConfigurationSection section)
    {
        Grenade grenade = new Grenade();
        grenade.setName(section.getString("display_name"));
        Material material = Material.getMaterial(section.getString("material"));
        if (material == null) {
            FrozenGun.plugin.getLogger().info(section.getString("material") + " was not found...");
            return;
        }
        grenade.setVelocityMultiplier(section.getDouble("velocity_multiplier"));
        grenade.setDetonationTime(section.getDouble("detonation_time"));
        grenade.setMaterial(material);
        grenade.setPower((float) section.getDouble("power"));
        WeaponManager.addWeapon(grenade);
    }

    public static void loadRailgun(ConfigurationSection section)
    {
        Railgun gun = new Railgun();
        gun.setName(section.getString("display_name"));
        gun.setMaterial(Material.getMaterial(section.getString("material")));
        gun.setRange(section.getDouble("range"));
        gun.setDamage(section.getDouble("damage"));
        gun.setCooldown(section.getDouble("cooldown"));
        gun.setHitbox(section.getDouble("hitbox", 1D));

        List<?> pass_through = section.getList("pass_through");
        if (pass_through != null) {
            List<String> pass_through_strings = (List<String>) pass_through;
            pass_through_strings.forEach((string) -> {
                Material material = null;
                try {
                    material = Material.valueOf(string);
                } catch (IllegalArgumentException e) {
                    FrozenGun.plugin.getLogger().warning("The material \"" + string + "\" was not found...");
                }
                if (material != null)
                    gun.addPassthroughMaterial(material);
            });
        }

        WeaponManager.addWeapon(gun);
    }

    public static void save()
    {
        ConfigLoader.saveGunConfig();
    }

}
