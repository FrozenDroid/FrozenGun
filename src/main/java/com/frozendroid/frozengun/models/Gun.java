package com.frozendroid.frozengun.models;

import com.frozendroid.frozengun.utils.Vector3D;
import org.bukkit.Material;
import java.util.HashSet;
import java.util.Set;

public abstract class Gun extends Weapon {

    protected Set<Material> passthroughMaterials = new HashSet<>();
    protected double damage;
    protected double cooldown;
    protected double range;
    protected double lastShot;
    public abstract void shoot();

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public double getCooldown() {
        return cooldown;
    }

    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void addPassthroughMaterial(Material material)
    {
        passthroughMaterials.add(material);
    }

    public double getCooldownTime()
    {
        double time = cooldown*1000-(System.currentTimeMillis()-lastShot);
        return time >= 0? time: 0;
    }

    public boolean canShoot()
    {
        return getCooldownTime() <= 0;
    }

    protected static boolean hasIntersection(Vector3D p1, Vector3D p2, Vector3D min, Vector3D max) {
        final double epsilon = 0.0001f;

        Vector3D d = p2.subtract(p1).multiply(0.5);
        Vector3D e = max.subtract(min).multiply(0.5);
        Vector3D c = p1.add(d).subtract(min.add(max).multiply(0.5));
        Vector3D ad = d.abs();

        if (Math.abs(c.x) > e.x + ad.x)
            return false;
        if (Math.abs(c.y) > e.y + ad.y)
            return false;
        if (Math.abs(c.z) > e.z + ad.z)
            return false;

        if (Math.abs(d.y * c.z - d.z * c.y) > e.y * ad.z + e.z * ad.y + epsilon)
            return false;
        if (Math.abs(d.z * c.x - d.x * c.z) > e.z * ad.x + e.x * ad.z + epsilon)
            return false;
        if (Math.abs(d.x * c.y - d.y * c.x) > e.x * ad.y + e.y * ad.x + epsilon)
            return false;

        return true;
    }

    public Set<Material> getPassthroughMaterials() {
        return passthroughMaterials;
    }

    public void setPassthroughMaterials(Set<Material> passthroughMaterials) {
        this.passthroughMaterials = passthroughMaterials;
    }

}
