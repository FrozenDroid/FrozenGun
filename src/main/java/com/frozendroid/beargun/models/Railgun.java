package com.frozendroid.beargun.models;

import com.darkblade12.particleeffect.ParticleEffect;
import com.frozendroid.beargun.BearGun;
import com.frozendroid.beargun.MinigameManager;
import com.frozendroid.beargun.events.PlayerShotEvent;
import com.frozendroid.beargun.utils.Vector3D;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class Railgun extends Gun {

    @Override
    public void shoot() {
        lastShot = System.currentTimeMillis();

        System.out.println("Damage: " + this.getDamage());

        passthroughMaterials.forEach(material1 -> {
            System.out.println("Pass through: " + material1.name());
        });

        List<Block> los = player.getLineOfSight(passthroughMaterials, (int) this.getRange());
        double block_distance = los.get(los.size()-1).getLocation().distance(player.getLocation());

        Location start = player.getEyeLocation();
        Vector increase = start.getDirection();

        for (int counter = 0; counter < block_distance-1; counter++) {
            Location point = start.add(increase);
            ParticleEffect.CRIT.display(0F, 0F, 0F, 0F, 1, point, 200D);
        }

        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 1, 0.8F);

        Location observerPos = player.getEyeLocation();
        Vector3D observerDir = new Vector3D(observerPos.getDirection());

        Vector3D observerStart = new Vector3D(observerPos);
        Vector3D observerEnd = observerStart.add(observerDir.multiply(this.getRange()));

        int i = 0;
        for (Player target : player.getWorld().getPlayers()) {
            Vector3D targetPos = new Vector3D(target.getLocation());
            Vector3D minimum = targetPos.add(-0.5, 0, -0.5);
            Vector3D maximum = targetPos.add(0.5, 1.80, 0.5);

            if (target != player.getPlayer() && hasIntersection(observerStart, observerEnd, minimum, maximum)) {
                if (block_distance > target.getPlayer().getLocation().distance(player.getLocation()) &&
                        this.player.getMatch().findPlayer(target.getUniqueId()) != null) {
                    final int finalI = i;
                    Bukkit.getScheduler().runTaskLater(BearGun.plugin, () -> {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1 + (finalI * 0.25F));
                    }, 2 + (i*2));
                    target.damage(this.getDamage(), player.getPlayer());
                    PlayerShotEvent event = new PlayerShotEvent();
                    event.setShooter(player);
                    event.setVictim(MinigameManager.getPlayer(target));
                    event.setGun(this);
                    BearGun.plugin.getServer().getPluginManager().callEvent(event);
                }
            }
        }
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    @Override
    public Weapon clone() {
        Railgun railgun = new Railgun();
        railgun.setPassthroughMaterials(this.getPassthroughMaterials());
        railgun.setName(this.getName());
        railgun.setRange(this.getRange());
        railgun.setMaterial(this.getMaterial());
        railgun.setCooldown(this.getCooldown());
        railgun.setDamage(this.getDamage());

        return railgun;
    }
}
