package com.frozendroid.frozengun.configs;

import com.frozendroid.frozengun.FrozenGun;
import com.frozendroid.frozengun.MinigameManager;
import com.frozendroid.frozengun.WeaponManager;
import com.frozendroid.frozengun.models.Arena;
import com.frozendroid.frozengun.models.Spawn;
import com.frozendroid.frozengun.models.Weapon;
import com.frozendroid.frozengun.models.objectives.MostKillObjective;
import com.frozendroid.frozengun.models.objectives.TotalKillObjective;
import com.frozendroid.frozengun.utils.ConfigLoader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ArenaConfig {


    private static FileConfiguration config;

    public static FileConfiguration get()
    {
        return config;
    }

    public static boolean loadArenas()
    {
        config = ConfigLoader.getArenaConfig();
        ConfigurationSection arenasection = config.getConfigurationSection("arenas");

        if (arenasection == null) return false;

        Set<String> list = arenasection.getKeys(false);

        List<Integer> keys = new ArrayList<>();
        HashMap<Integer, ConfigurationSection> arenasections = new HashMap<>();

        list.forEach((String key) -> keys.add(Integer.parseInt((String) key)));
        keys.forEach((Integer key) -> arenasections.put(key, config.getConfigurationSection("arenas."+key)));

        try {
            arenasections.forEach((Integer key, ConfigurationSection section) -> {
                Arena arena = new Arena();
                arena.setId(key);
                arena.setName(section.getString("name"));
                arena.setMinPlayers(section.getInt("min_players"));
                arena.setMaxPlayers(section.getInt("max_players"));
                arena.getLobby().setCountdownTime(section.getInt("start_time"));
                arena.setRunSpeed(Float.parseFloat(String.valueOf(section.getDouble("running_speed", 0.2D))));

                // load lobby location (optional)
                ConfigurationSection lobbyLocSection = section.getConfigurationSection("lobbyLocation");
                if (lobbyLocSection != null) {
                    World lobbyWorld = Bukkit.getServer().getWorld(lobbyLocSection.getString("world"));
                    double lobbyX = lobbyLocSection.getDouble("x");
                    double lobbyY = lobbyLocSection.getDouble("y");
                    double lobbyZ = lobbyLocSection.getDouble("z");
                    double lobbyYaw = lobbyLocSection.getDouble("yaw");
                    double lobbyPitch = lobbyLocSection.getDouble("pitch");
                    arena.getLobby().setLocation(
                            new Location(lobbyWorld, lobbyX, lobbyY, lobbyZ, (float) lobbyYaw, (float) lobbyPitch)
                    );
                }

                // Load options
                ConfigurationSection options = section.getConfigurationSection("options");
                arena.setFallingDamage(options.getBoolean("falling_damage"));

                boolean announceKillingSpree = section.getBoolean("killing_spree");
                if (announceKillingSpree) {
                    arena.setAnnounceKillingSpree(true);
                    arena.setKillingSpreeDelay(section.getDouble("spree_delay"));
                }

                ArrayList<String> weapon_strings = (ArrayList<String>) section.getList("weapons");
                weapon_strings.forEach(weapon_string -> {
                    Weapon weapon = WeaponManager.findByName(weapon_string);
                    if (weapon != null) {
                        arena.addWeapon(weapon);
                    } else {
                        FrozenGun.warn("The weapon \"" + weapon_string + "\" was not found...");
                    }
                });

                section.getConfigurationSection("objectives").getKeys(false).stream().forEach((key_) -> {
                    switch (key_) {
                        case "total_kills":
                            TotalKillObjective totalKillObjective = new TotalKillObjective();
                            totalKillObjective.setGoal(section.getConfigurationSection("objectives").getInt(key_));
                            arena.setObjective(totalKillObjective);
                            break;
                        case "most_kills":
                            MostKillObjective mostKillObjective = new MostKillObjective();
                            mostKillObjective.setGoal(section.getConfigurationSection("objectives").getInt(key_));
                            arena.setObjective(mostKillObjective);
                            break;
                    }
                });

                section.getStringList("spawns").forEach((String spawnJSON) -> {
                    Spawn spawn = new Spawn();
                    JSONObject spawn_ = new JSONObject(spawnJSON);

                    World world = FrozenGun.plugin.getServer().getWorld(spawn_.getString("world"));
                    double x = spawn_.getDouble("x");
                    double y = spawn_.getDouble("y");
                    double z = spawn_.getDouble("z");
                    float yaw = spawn_.getBigDecimal("yaw").floatValue();
                    float pitch = spawn_.getBigDecimal("pitch").floatValue();
                    spawn.setLocation(new Location(world, x, y, z, yaw, pitch));
                    arena.addSpawn(spawn);
                });
                MinigameManager.addArena(arena);
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
            FrozenGun.error("Arena config is invalid!");
            return false;
        }


        return true;
    }

    public static void save()
    {
        ConfigLoader.saveArenaConfig();
    }

}
