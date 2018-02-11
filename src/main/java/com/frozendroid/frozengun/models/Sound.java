package com.frozendroid.frozengun.models;

import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class Sound {

    protected File file;
    protected SoundType type;
    protected Set<Player> listeners = new HashSet<>();

    public SoundType getType() {
        return type;
    }

    public void setType(SoundType type) {
        this.type = type;
    }

    public Set<Player> getListeners() {
        return listeners;
    }

    public void setListeners(Set<Player> listeners) {
        this.listeners = listeners;
    }

    Sound(File file, SoundType type) {
        this.file = file;
        this.type = type;
    }

    abstract void load();
    abstract void play();
    abstract void stop();

}
