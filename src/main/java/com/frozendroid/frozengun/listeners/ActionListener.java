package com.frozendroid.frozengun.listeners;

import com.frozendroid.frozengun.FrozenGun;
import com.frozendroid.frozengun.Messenger;
import com.frozendroid.frozengun.MinigameManager;
import com.frozendroid.frozengun.events.MessageEvent;
import com.frozendroid.frozengun.events.PlayerJoinedLobbyEvent;
import com.frozendroid.frozengun.events.PlayerLeaveLobbyEvent;
import com.frozendroid.frozengun.models.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.SplashPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Optional;

public class ActionListener implements Listener {

    public ActionListener(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Block block = event.getBlock();
        Sign sign = (Sign) event.getBlock().getState();
        String[] lines = event.getLines();
        Player player = event.getPlayer();

        if (!lines[0].equals("[frozengun]"))
            return;

        if (!player.hasPermission("railgun.create.sign") && !player.isOp()) {
            player.sendMessage("You're not allowed to create a join sign.");
            block.breakNaturally();
            return;
        }

        if (lines[1].equals("")) {
            player.sendMessage("You need to fill in the arena name in the second row.");
            block.breakNaturally();
            return;
        }

        String arena_name = lines[1];

        Arena arena = Arena.getByName(arena_name);

        if (arena == null) {
            player.sendMessage("Arena not found.");
            block.breakNaturally();
            return;
        }

        event.setCancelled(true);

        sign.setLine(0, ChatColor.DARK_AQUA + "[FrozenGun]");
        sign.setLine(1, ChatColor.AQUA + "Join");
        sign.setLine(2, ChatColor.BLACK + arena.getName());
        sign.update();

    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        Player player = event.getPlayer();

        MinigamePlayer minigamePlayer = MinigameManager.getPlayer(player);

        if (minigamePlayer == null)
            return;

        Match match = minigamePlayer.getMatch();

        if (match == null)
            return;

        Weapon weapon = null;

        ArrayList<Weapon> weapons = minigamePlayer.getWeapons();
        for (Weapon _weapon : weapons) {
            if (_weapon.getMaterial() == event.getItemDrop().getItemStack().getType()) {
                FrozenGun.info("Grenade is not null!!");
                weapon = _weapon;
            }
        }

        if (weapon == null)
            return;


        if (weapon instanceof Grenade) {
            Grenade grenade = (Grenade) weapon;
            grenade.throwWeapon(item);
        }


    }

    // This is neccesary because some plugins like to go full retard and block entity damage after teleport or respawn.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        MinigamePlayer player = MinigameManager.getPlayer((Player) event.getEntity());

        if (player == null)
            return;

        if (!player.isInMatch())
            return;

        if (player.getHealth() - event.getDamage() <= 0) {
            event.setCancelled(true);
            player.respawn();
        } else {
            event.setCancelled(false);
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player))
            return;

        MinigamePlayer player = MinigameManager.getPlayer((Player) event.getEntity().getShooter());

        if (player == null)
            return;

        Match match = player.getMatch();

        if (match == null)
            return;

        if (event.getEntity().getType() == EntityType.SPLASH_POTION) {
            Bukkit.broadcastMessage("potion has been thrown");
            SplashPotion potion = (SplashPotion) event.getEntity();
            ItemStack item = potion.getItem();

            Item _item = potion.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
            _item.teleport(player.getEyeLocation());
            _item.getLocation().setDirection(player.getEyeLocation().getDirection());
            Bukkit.getPluginManager().callEvent(new PlayerDropItemEvent(player.getPlayer(), _item));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent evt) {
        if (
                evt.getAction() == Action.RIGHT_CLICK_BLOCK &&
                        (
                                evt.getClickedBlock().getType() == Material.WALL_SIGN ||
                                        evt.getClickedBlock().getType() == Material.SIGN_POST
                        )
                ) {
            Sign sign = (Sign) evt.getClickedBlock().getState();
            String[] lines = sign.getLines();

            if (!lines[0].equals(ChatColor.DARK_AQUA + "[FrozenGun]"))
                return;

            if (lines[1].equals("")) {
                evt.getPlayer().sendMessage("The second row is empty..." +
                        " so we'll never be able to find the arena to join.");
                return;
            }

            String arena_name = lines[2];
            Arena arena = Arena.getByName(arena_name);

            if (arena == null) {
                evt.getPlayer().sendMessage(Messenger.infoMsg("Couldn't find the arena :("));
                return;
            }

            MinigamePlayer player = MinigameManager.getPlayerOrNewIfNotExists(evt.getPlayer());

            Optional<Match> matchOptional = MinigameManager.findMatchByArena(arena);
            if (matchOptional.isPresent()) {
                Match match = matchOptional.get();
                if (!match.findPlayer(player.getUniqueId()).isPresent()) {
                    MinigameManager.addPlayer(player);
                    player.saveCurrentState();
                    player.join(match);
                    return;
                } else {
                    player.sendMessage(Messenger.infoMsg("You're already in this match!"));
                    return;
                }
            }

            Lobby arenaLobby = arena.getLobby();
            arenaLobby.startWaitingTimerIfNotStarted();
            if (player.getLobby() == arenaLobby || player.inLobby()) {
                player.leaveLobby(arenaLobby);
                return;
            }

            arenaLobby.addPlayer(player);
            MinigameManager.addPlayer(player);
            player.saveCurrentState();
            player.setLobby(arenaLobby);
            if (arenaLobby.getLocation().isPresent()) {
                player.teleport(arenaLobby.getLocation().get());
            }
            Bukkit.getServer().getPluginManager().callEvent(new PlayerJoinedLobbyEvent(player, arenaLobby, player));
            return;
        }

        if (evt.getAction() == Action.RIGHT_CLICK_BLOCK || evt.getAction() == Action.RIGHT_CLICK_AIR) {
            MinigamePlayer player = MinigameManager.getPlayer(evt.getPlayer());

            if (player == null || !player.isInMatch()) return;

            Weapon weapon = player.getWeaponInHand();

            if (weapon == null) return;

            if (weapon instanceof Gun) {
                Gun gun = (Gun) weapon;
                if (gun.canShoot())
                    gun.shoot();
            }

        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause() != DamageCause.ENTITY_EXPLOSION) return;

        if (event.getEntityType() == EntityType.PLAYER) {
            Player player = (Player) event.getEntity();

            MinigamePlayer minigamePlayer = MinigameManager.getPlayer(player);

            if (minigamePlayer == null || minigamePlayer.getMatch() == null) return;


            if (!minigamePlayer.getMatch().getArena().hasFallingDamage() && event.getCause() == DamageCause.FALL) {
                event.setDamage(0);
                event.setCancelled(true);
                return;
            }

            event.setDamage(9999);
        }
    }

}
