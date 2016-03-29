package com.frozendroid.beargun;

import com.frozendroid.beargun.models.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MinigameManager {

    private static List<Match> matches = new ArrayList<>();
    private static List<Arena> arenas = new ArrayList<>();
    private static List<Gun_old> guns = new ArrayList<>();
    private static List<Weapon> weapons = new ArrayList<>();
    private static List<MinigamePlayer> players = new ArrayList<>();

    public static void reset()
    {
        matches.clear();
        arenas.clear();
        guns.clear();
        players.clear();
    }

    public static List<Match> getMatches() {
        return matches;
    }

    public static void setMatches(List<Match> matches) {
        MinigameManager.matches = matches;
    }

    public static void addMatch(Match match)
    {
        MinigameManager.matches.add(match);
    }

    public static List<Arena> getArenas(){
        return arenas;
    }

    public static void addArena(Arena arena)
    {
        arenas.add(arena);
    }

    public static List<Gun_old> getGuns() {
        return guns;
    }

    public static void addGun(Gun_old gun)
    {
        guns.add(gun);
    }

    public static List<MinigamePlayer> getPlayers() {
        return players;
    }

    public static MinigamePlayer getPlayer(Player player)
    {
        return players.stream().filter((player_) -> player_.getPlayer().equals(player)).findFirst().orElse(null);
    }

    public static List<Weapon> getWeapons()
    {
        return weapons;
    }

    public static void addWeapon(Weapon weapon)
    {
        weapons.add(weapon);
    }

    public static void setWeapons(List<Weapon> weapons)
    {
        MinigameManager.weapons = weapons;
    }

    public static void endAllMatches()
    {
        ArrayList<Match> array = new ArrayList();
        array.addAll(matches);
        array.forEach(Match::end);
    }

    public static void endAllQueues()
    {
        arenas.forEach(arena -> {
            Queue queue = arena.getQueue();
            if (queue != null)
                queue.stop();
        });
    }

    public static void addPlayers(List<MinigamePlayer> players_)
    {
        players.addAll(players_);
    }

    public static void addPlayer(MinigamePlayer player)
    {
        players.add(player);
    }

    public static boolean removePlayer(MinigamePlayer player) {
        return players.remove(player);
    }


}
