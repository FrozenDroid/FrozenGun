package com.frozendroid.beargun;

import com.frozendroid.beargun.models.Arena;
import com.frozendroid.beargun.models.Gun;
import com.frozendroid.beargun.models.Match;
import com.frozendroid.beargun.models.MinigamePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MinigameManager {

    private static List<Match> matches = new ArrayList<>();
    private static List<Arena> arenas = new ArrayList<>();
    private static List<Gun> guns = new ArrayList<>();
    private static List<MinigamePlayer> players = new ArrayList<>();

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

    public static List<Gun> getGuns() {
        return guns;
    }

    public static void addGun(Gun gun)
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
