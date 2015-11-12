package com.frozendroid.beargun.models;

import com.frozendroid.beargun.MinigameManager;

import java.util.ArrayList;
import java.util.List;

public class Arena {

    private Match match = null;

    private Integer minPlayers;
    private Integer maxPlayers;
    private Integer id;
    private List<Spawn> spawns = new ArrayList<>();
    private String name;
    private Gun gun;
    private Queue queue;
    private List<GameObjective> objectives = new ArrayList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Spawn> getSpawns() {
        return spawns;
    }

    public void addSpawn(Spawn spawn)
    {
        spawns.add(spawn);
    }

    public Gun getGun() {
        return gun;
    }

    public void setGun(Gun gun) {
        this.gun = gun;
    }

    public static Arena getByName(String name)
    {
        return MinigameManager.getArenas().stream().filter(arena -> arena.getName().equalsIgnoreCase(name)).findFirst()
                .orElse(null);
    }

    public static Arena getById(Integer id)
    {
        return MinigameManager.getArenas().stream().filter(arena -> arena.getId().equals(id)).findFirst().orElse(null);
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(Integer minPlayers) {
        this.minPlayers = minPlayers;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public boolean hasQueue()
    {
        return this.queue != null;
    }

    public List<GameObjective> getObjectives()
    {
        return objectives;
    }

    public void setObjectives(List<GameObjective> objectives)
    {
        this.objectives = objectives;
    }

    public void addObjective(GameObjective objective)
    {
        objectives.add(objective);
    }
}
