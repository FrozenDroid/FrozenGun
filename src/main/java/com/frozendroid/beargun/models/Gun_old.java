package com.frozendroid.beargun.models;

import com.frozendroid.beargun.MinigameManager;
import com.frozendroid.beargun.utils.Vector3D;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

public class Gun_old {

    private Material material;
    private Double damage = 0D;
    private String name;
    private Double cooldown = 0D;
    private Long lastShot = 0L;
    private MinigamePlayer player;
    private Set<Material> passthroughMaterials = new HashSet<>();

    public Gun_old()
    {

    }

    public Gun_old(Gun_old gun, MinigamePlayer player)
    {
        this.passthroughMaterials = gun.getPassthroughMaterials();
        this.name       = gun.getName();
        this.material   = gun.getMaterial();
        this.damage     = gun.getDamage();
        this.cooldown   = gun.getCooldown();
        this.player     = player;
    }

    public double getCooldown() {
        return cooldown;
    }

    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }

    public Material getMaterial() {
        return material;
    }

    public void shoot()
    {
    }

    public double getCooldownTime()
    {
        double time = cooldown*1000-(System.currentTimeMillis()-lastShot);
        return time >= 0? time: 0;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public boolean canShoot()
    {
        return getCooldownTime() <= 0;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Gun_old findByName(String name) {
        return MinigameManager.getGuns().stream().filter(gun -> gun.getName().equalsIgnoreCase(name)).findFirst()
                .orElse(null);
    }

    public MinigamePlayer getPlayer() {
        return player;
    }

    public void setPlayer(MinigamePlayer player) {
        this.player = player;
    }

    public Set<Material> getPassthroughMaterials() {
        return passthroughMaterials;
    }

    public void setPassthroughMaterials(Set<Material> passthroughMaterials) {
        this.passthroughMaterials = passthroughMaterials;
    }
}
