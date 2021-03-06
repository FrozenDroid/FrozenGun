package com.frozendroid.frozengun;

import com.frozendroid.frozengun.commands.CommandHandler;
import com.frozendroid.frozengun.configs.ArenaConfig;
import com.frozendroid.frozengun.configs.MessagesConfig;
import com.frozendroid.frozengun.configs.WeaponConfig;
import com.frozendroid.frozengun.events.MessageEvent;
import com.frozendroid.frozengun.events.PlayerJoinedLobbyEvent;
import com.frozendroid.frozengun.events.PlayerJoinedMatchEvent;
import com.frozendroid.frozengun.events.PlayerLeaveLobbyEvent;
import com.frozendroid.frozengun.listeners.ActionListener;
import com.frozendroid.frozengun.listeners.MessageListener;
import com.frozendroid.frozengun.listeners.PlayerListener;
import com.frozendroid.frozengun.models.Arena;
import com.frozendroid.frozengun.models.Weapon;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class FrozenGun extends JavaPlugin {

    public static Plugin plugin;
    private static long lastModified = 0L;
    private static boolean hasFoundNewFile = false;
    private static boolean useNoteBlockAPI = false;

    private static FileConfiguration config;
    private static boolean devMode = false;
    private static boolean debugMode = false;

    private static ConsoleCommandSender console;

    public FrozenGun() {
        plugin = this;
        lastModified = getFile().lastModified();
    }

    public static boolean useNoteBlockAPI() {
        return useNoteBlockAPI;
    }

    public static void setUseNoteBlockAPI(boolean useNoteBlockAPI) {
        FrozenGun.useNoteBlockAPI = useNoteBlockAPI;
    }

    private static boolean detectNoteBlockAPI() {
        setUseNoteBlockAPI(false);
        if (plugin.getServer().getPluginManager().isPluginEnabled("NoteBlockAPI")) {
            setUseNoteBlockAPI(true);
            return true;
        }
        return false;
    }

    public static void debug(String msg) {
        if (inDebugMode()) {
            FrozenGun.info("DEBUG: " + msg);
        }
    }

    public static void info(String msg) {
        console.sendMessage(Messenger.infoMsg(msg));
    }

    public static void warn(String msg) {
        plugin.getLogger().warning(msg);
    }

    public static void error(String msg) {
        plugin.getLogger().severe(msg);
    }

    public static boolean inDebugMode() {
        return debugMode;
    }

    public static boolean inDevelopmentMode() {
        return devMode;
    }

    @Override
    public void onEnable() {
        console = this.getServer().getConsoleSender();

        // Disable plugin if it can't find the main config values.
        if (!loadMainConfig()) {
            error("Missing required config values, disabling...");
            setEnabled(false);
            return;
        }

        if (detectNoteBlockAPI()) {
            info("NoteBlockAPI was found so we're using it.");
        }

        if (inDevelopmentMode()) {
            getServer().broadcastMessage(Messenger.infoMsg("Loaded plugin."));
        }

        if (inDebugMode()) {
            info("Debug mode is activated.");
        }

        reloadOnUpdate();

        Bukkit.getScheduler().runTaskLater(this, () -> {
            WeaponConfig.loadGuns();
            ArenaConfig.loadArenas();
            MessagesConfig.loadMessages();

            MessageEvent.checkLookupCaches(PlayerJoinedLobbyEvent.class);
            MessageEvent.checkLookupCaches(PlayerJoinedMatchEvent.class);
            MessageEvent.checkLookupCaches(PlayerLeaveLobbyEvent.class);


            for (Arena arena : MinigameManager.getArenas()) {
                FrozenGun.debug("Arena loaded: " + arena.getName());
            }

            for (Weapon weapon : WeaponManager.getWeapons()) {
                FrozenGun.debug("Weapon loaded: " + weapon.getName());
            }
        }, 1L);

        /*
        Check caches here so that lookups don't have to be computed in the constructor.
         */

        new MessageListener(this);
        new ActionListener(this);
        new PlayerListener(this);
        new CommandHandler(this);
    }

    private void reloadOnUpdate() {
        if (inDevelopmentMode()) {
            info(
                    "Development mode is true, therefore we'll reload the server when " +
                            "the JAR's modified date is newer."
            );

            hasFoundNewFile = false;

            getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
                if (hasFoundNewFile)
                    return;

                long modifiedMostRecent = getFile().lastModified();
                if (modifiedMostRecent > lastModified) {
                    hasFoundNewFile = true;
                    info("Found a newer file, waiting a second to reload the server.");
                    getServer().getScheduler().runTaskLater(this, () -> {
                        if (inDebugMode())
                            getServer().broadcastMessage(Messenger.infoMsg("Auto-reloading..."));
                        info("Reloading...");
                        lastModified = getFile().lastModified();
                        this.getServer().reload();
                    }, 20);
                }
            }, 0, 10);
        }
    }

    private boolean loadMainConfig() {
        this.saveDefaultConfig();
        this.reloadConfig();
        config = this.getConfig();

        try {
            FrozenGun.devMode = config.getBoolean("devMode");
            FrozenGun.debugMode = config.getBoolean("debugMode");
        } catch (NullPointerException ex) {
            return false;
        }

        return true;
    }

    @Override
    public void onDisable() {
        MinigameManager.endAllMatches();
    }

}
