package com.frozendroid.frozengun.models;

import com.frozendroid.frozengun.FrozenGun;
import com.frozendroid.frozengun.MinigameManager;
import com.frozendroid.frozengun.events.PlayerShotEvent;
import com.frozendroid.frozengun.utils.Vector3D;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.inventivetalent.particle.ParticleEffect;

import java.util.List;

public class Railgun extends Gun {

    @Override
    public void shoot() {
        lastShot = System.currentTimeMillis();

        FrozenGun.debug("Damage: " + this.getDamage());

        List<Block> los = player.getLineOfSight(passthroughMaterials, (int) this.getRange());
        double block_distance = los.get(los.size()-1).getLocation().distance(player.getLocation());

        Location start = player.getEyeLocation();
        Vector increase = start.getDirection();

        for (int counter = 0; counter < block_distance-1; counter++) {
            Location point = start.add(increase);
            ParticleEffect.CRIT.send(Bukkit.getOnlinePlayers(), point, 0, 0, 0, 0.1, 1);
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

            boolean hasIntersection = hasIntersection(observerStart, observerEnd, minimum, maximum);
            boolean notTargetingSelf = target != player.getPlayer();
            if (notTargetingSelf && hasIntersection) {
                boolean inGunRange = block_distance > target.getPlayer().getLocation().distance(player.getLocation());
                boolean playerNotNull = this.player.getMatch().findPlayer(target.getUniqueId()) != null;
                if (inGunRange && playerNotNull) {
                    final int finalI = i;
                    Bukkit.getScheduler().runTaskLater(FrozenGun.plugin, () -> {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1 + (finalI * 0.25F));
                    }, 2 + (i*2));
                    target.damage(this.getDamage(), player.getPlayer());
                    PlayerShotEvent event = new PlayerShotEvent();
                    event.setShooter(player);
                    event.setVictim(MinigameManager.getPlayer(target));
                    event.setGun(this);
                    FrozenGun.plugin.getServer().getPluginManager().callEvent(event);
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
        railgun.setLore(this.getLore());

        return railgun;
    }
}
