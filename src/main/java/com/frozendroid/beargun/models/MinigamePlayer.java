package com.frozendroid.beargun.models;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MinigamePlayer {

    private Gun gun;
    private Player player;
    private Match match;

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
        setGun(getGun());
    }


}
