package com.frozendroid.beargun.models;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Spawn {

    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isFeasible()
    {
        return this.getLocation().getWorld().getEntities().stream().filter(
                (entity -> entity instanceof Player)
        ).noneMatch(
                entity -> entity.getLocation().distance(location) <= 5
        );
    }

}
