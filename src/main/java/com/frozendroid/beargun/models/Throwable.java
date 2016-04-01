package com.frozendroid.beargun.models;

import org.bukkit.entity.Item;

public abstract class Throwable extends Weapon {
    private double velocityMultiplier;

    public abstract void throwWeapon(Item item);

    public double getVelocityMultiplier() {
        return velocityMultiplier;
    }

    public void setVelocityMultiplier(double velocityMultiplier) {
        this.velocityMultiplier = velocityMultiplier;
    }

}
