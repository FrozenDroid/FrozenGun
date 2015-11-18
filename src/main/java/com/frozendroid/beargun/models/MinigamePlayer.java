package com.frozendroid.beargun.models;

import com.frozendroid.beargun.BearGun;
import com.frozendroid.beargun.MinigameManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MinigamePlayer {

    private Gun gun;
    private Player player;
    private Match match;
    private Location lastLocation;

    public MinigamePlayer(Player player)
    {
        this.player = player;
    }

    public Gun getGun() {
        return gun;
    }

    public void setGun(Gun gun) {
        ItemStack item = new ItemStack(gun.getMaterial());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(gun.getName());
        List<String> lore = new ArrayList<String>();
        lore.add("Does "+gun.getDamage()/2+" hearts of damage.");
        meta.setLore(lore);
        item.setItemMeta(meta);
        player.getInventory().addItem(item);
        this.gun = gun;
    }

    public void removeGun()
    {
        player.getInventory().remove(gun.getMaterial());
    }

    public Player getPlayer() {
        return player;
    }

    public Match getMatch() {
        return match;
    }

    public boolean isInMatch()
    {
        return match != null;
    }

    public void join(Match match) {
        this.match = match;
    }

    public void respawn(Match match)
    {
        Player player = this.getPlayer();
        player.setHealth(20);
        player.teleport(match.getFeasibleSpawn().getLocation());
    }

    public void leave(Match match)
    {
        this.getPlayer().setHealth(20);
        this.getPlayer().teleport(getLastLocation());
        this.removeGun();
        MinigameManager.removePlayer(this);
    }

    public Location getLastLocation()
    {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation)
    {
        this.lastLocation = lastLocation;
    }
}
