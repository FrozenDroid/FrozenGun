package com.frozendroid.frozengun.models;

import com.frozendroid.frozengun.MinigameManager;
import com.frozendroid.frozengun.configs.ArenaConfig;
import com.frozendroid.frozengun.models.objectives.GameObjective;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Arena {

    private Match match = null;

    private Integer minPlayers;
    private Integer maxPlayers;
    private Integer id;
    private List<Spawn> spawns = new ArrayList<>();
    private String name;
    private Integer startingTime = 30;
    private ArrayList<Weapon> weapons = new ArrayList<>();
    private boolean occupied = false;
    private Queue queue;
    private List<GameObjective> objectives = new ArrayList<>();
    private boolean announceKillingSpree = false;
    private Double killingSpreeDelay = 0D;

    // Options
    private boolean fallingDamage = false;

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

    public static Arena getByName(String name)
    {
        return MinigameManager.getArenas().stream().filter(arena -> arena.getName().equalsIgnoreCase(name)).findFirst()
                .orElse(null);
    }

    public static Arena getById(Integer id)
    {
        return MinigameManager.getArenas().stream().filter(arena -> arena.getId().equals(id)).findFirst().orElse(null);
    }


    public static Arena create()
    {
        return new Arena();
    }

    public void save()
    {
        ConfigurationSection section = ArenaConfig.get().getConfigurationSection("arenas");
        Map<String, Object> ids = section.getValues(false);

        System.out.println(ids.size()+1);
        ConfigurationSection arenaSection = section.createSection(""+ (ids.size() + 1));
        arenaSection.set("name", name);
        arenaSection.set("min_players", minPlayers);
        arenaSection.set("max_players", maxPlayers);
        ArrayList<String> weapons = new ArrayList<>();
        this.weapons.forEach(weapon -> weapons.add(weapon.getName()));
        arenaSection.set("weapons", weapons);
        arenaSection.set("start_time", startingTime);

        // Options section
        ConfigurationSection options = arenaSection.createSection("options");
        options.set("falling_damage", fallingDamage);

        List<String> spawnList = new ArrayList<>();
        spawns.forEach((spawn -> spawnList.add(spawn.toJson())));
        arenaSection.set("spawns", spawnList);

        ConfigurationSection objectivesSection = arenaSection.createSection("objectives");

        objectives.forEach((objective) -> objectivesSection.set(objective.getTypeName(), objective.getGoal()));

        ArenaConfig.save();
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

    public boolean isAnnounceKillingSpree()
    {
        return announceKillingSpree;
    }

    public void setAnnounceKillingSpree(boolean announceKillingSpree)
    {
        this.announceKillingSpree = announceKillingSpree;
    }

    public Double getKillingSpreeDelay()
    {
        return killingSpreeDelay;
    }

    public void setKillingSpreeDelay(Double killingSpreeDelay)
    {
        this.killingSpreeDelay = killingSpreeDelay;
    }

    public Integer getStartingTime()
    {
        return startingTime;
    }

    public void setStartingTime(Integer startingTime)
    {
        this.startingTime = startingTime;
    }

    public boolean isOccupied()
    {
        return occupied;
    }

    public void setOccupied(boolean occupied)
    {
        this.occupied = occupied;
    }

    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    public void setWeapons(ArrayList<Weapon> weapons) {
        this.weapons = weapons;
    }

    public void addWeapon(Weapon weapon)
    {
        this.weapons.add(weapon);
    }

    public boolean hasFallingDamage() {
        return fallingDamage;
    }

    public void setFallingDamage(boolean fallingDamage) {
        this.fallingDamage = fallingDamage;
    }
}
