package com.frozendroid.bearquake.commands;

import com.frozendroid.bearquake.models.Gun;
import com.frozendroid.bearquake.MinigameManager;
import com.frozendroid.bearquake.models.Match;
import com.frozendroid.bearquake.models.MinigamePlayer;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CommandHandler implements CommandExecutor {

    private Plugin plugin;

    public CommandHandler(Plugin plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginCommand("railgun").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        if (args.length == 1) {
//            if (args[0].equalsIgnoreCase("init")) {
//                Player player = (Player) sender;
//                Match match = MinigameManager.getMatches().get(0);
////                match.addPlayer(player);
//                MinigamePlayer mgmplayer = match.findPlayer(player.getUniqueId());
//                Gun gun = new Gun();
//                gun.setCooldown(1.5);
//                gun.setDamage(5);
//                gun.setMaterial(Material.IRON_HOE);
//                gun.setName("THE REKKER");
//                mgmplayer.setGun(gun);
//
//
//
//            }
//        }
        return true;
    }

}
