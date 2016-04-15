package com.frozendroid.beargun.models;

import com.darkblade12.particleeffect.ParticleEffect;
import com.frozendroid.beargun.BearGun;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitTask;

public class Grenade extends Throwable {

    private float power;
    private BukkitTask task;
    private int taskCounter;
    private double detonationTime;

    public float getPower() {
        return power;
    }

    public void setPower(float radius) {
        this.power = radius;
    }

    public double getDetonationTime() {
        return detonationTime;
    }

    public void setDetonationTime(double detonationTime) {
        this.detonationTime = detonationTime;
    }

    @Override
    public void throwWeapon(Item item) {
        if (this.getPlayer() == null) {
            BearGun.plugin.getLogger().info("Player is null");
            return;
        }

        BearGun.plugin.getLogger().info("Grenade was thrown");

        item.setVelocity(item.getVelocity().multiply(this.getVelocityMultiplier()));

        final boolean[] moving = new boolean[1];
        moving[0] = true;

        task = Bukkit.getScheduler().runTaskTimer(BearGun.plugin, () -> {
            taskCounter++;

            if (item.getVelocity().length() < 0.1) {
                moving[0] = false;
            }

            if (moving[0]) {
                ParticleEffect.CRIT.display(0F, 0F, 0F, 0F, 1, item.getLocation(), 200D);
            }

            if (taskCounter >= this.getDetonationTime()*20) {
                task.cancel();
                Location location = item.getLocation();
                double x = location.getX();
                double y = location.getY();
                double z = location.getZ();
                item.getLocation().getWorld().createExplosion(x, y, z, this.getPower(), false, false);
                item.remove();
            }
        }, 0L, 1L);

    }

    public boolean isMoving(Item item)
    {
        return item.getVelocity().length() < 0.01;
    }

    @Override
    public Weapon clone()
    {
        Grenade grenade = new Grenade();
        grenade.setName(this.getName());
        grenade.setLore(this.getLore());
        grenade.setMaterial(this.getMaterial());
        grenade.setDetonationTime(this.getDetonationTime());
        grenade.setPower(this.getPower());
        grenade.setVelocityMultiplier(this.getVelocityMultiplier());
        return grenade;
    }

}
