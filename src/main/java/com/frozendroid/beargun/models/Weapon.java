package com.frozendroid.beargun.models;

import org.bukkit.Material;

public abstract class Weapon {

    protected String name;
    protected MinigamePlayer player;
    protected Material material;
    protected String[] lore;
    public abstract Weapon clone();

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MinigamePlayer getPlayer() {
        return player;
    }

    public void setPlayer(MinigamePlayer player) {
        this.player = player;
    }

    public String[] getLore() {
        return lore;
    }

    public void setLore(String[] lore) {
        this.lore = lore;
    }

}
