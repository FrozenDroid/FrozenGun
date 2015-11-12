package com.frozendroid.bearquake.models;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

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
