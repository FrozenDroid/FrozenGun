package com.frozendroid.beargun.configs;

import com.frozendroid.beargun.BearGun;
import com.frozendroid.beargun.MinigameManager;
import com.frozendroid.beargun.models.Arena;
import com.frozendroid.beargun.models.Gun;
import com.frozendroid.beargun.models.Spawn;
import com.frozendroid.beargun.models.objectives.KillObjective;
import com.frozendroid.beargun.utils.ConfigLoader;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.json.JSONObject;

import java.io.IOException;
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
        Set<String> list = arenasection.getKeys(false);

        List<Integer> keys = new ArrayList<>();
        HashMap<Integer, ConfigurationSection> arenasections = new HashMap<>();

        list.forEach((String key) -> keys.add(Integer.parseInt((String) key)));
        keys.forEach((Integer key) -> arenasections.put(key, config.getConfigurationSection("arenas."+key)));

        arenasections.forEach((Integer key, ConfigurationSection section) -> {
            Arena arena = new Arena();
            arena.setId(key);
            arena.setName(section.getString("name"));
            arena.setMinPlayers(section.getInt("min_players"));
            arena.setMaxPlayers(section.getInt("max_players"));
            arena.setGun(Gun.findByName(section.getString("gun")));

            section.getConfigurationSection("objectives").getKeys(false).stream().forEach((key_) -> {
                switch (key_) {
                    case "total_kills":
                        KillObjective objective = new KillObjective();
                        objective.setKillGoal(section.getConfigurationSection("objectives").getInt(key_));
                        arena.addObjective(objective);
                }
            });

            section.getStringList("spawns").forEach((String spawnJSON) -> {
                Spawn spawn = new Spawn();
                JSONObject spawn_ = new JSONObject(spawnJSON);


                World world = BearGun.plugin.getServer().getWorld(spawn_.getString("world"));
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

        return true;
    }

    public static void save()
    {
        try {
            config.save("arenas.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
