package com.frozendroid.frozengun.models;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.json.JSONObject;

public class Spawn {

    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    // TODO: Implement ray-tracing so that people don't spawn in location that someone has in sight
    public boolean isFeasible()
    {
        return this.getLocation().getWorld().getEntities().stream().filter(
                (entity -> entity instanceof Player)
        ).noneMatch(
                entity -> entity.getLocation().distance(location) <= 5
        );
    }

    public static Spawn fromLocation(Location location)
    {
        Spawn spawn = new Spawn();
        spawn.setLocation(location);
        return spawn;
    }

    public String toJson()
    {
        JSONObject json = new JSONObject();
        json.put("x", location.getX());
        json.put("y", location.getY());
        json.put("z", location.getZ());
        json.put("world", location.getWorld().getName());
        json.put("yaw", location.getYaw());
        json.put("pitch", location.getPitch());

        return json.toString();
    }

}
