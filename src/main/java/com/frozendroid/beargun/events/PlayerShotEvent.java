package com.frozendroid.beargun.events;

import com.frozendroid.beargun.models.Gun;
import com.frozendroid.beargun.models.MinigamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerShotEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private MinigamePlayer victim;
    private MinigamePlayer shooter;
    private Gun gun;

    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    public MinigamePlayer getVictim()
    {
        return victim;
    }

    public void setVictim(MinigamePlayer victim)
    {
        this.victim = victim;
    }

    public MinigamePlayer getShooter()
    {
        return shooter;
    }

    public void setShooter(MinigamePlayer shooter)
    {
        this.shooter = shooter;
    }

    public Gun getGun()
    {
        return gun;
    }

    public void setGun(Gun gun)
    {
        this.gun = gun;
    }
}
