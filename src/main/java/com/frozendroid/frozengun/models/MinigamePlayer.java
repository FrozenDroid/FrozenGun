package com.frozendroid.frozengun.models;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;

import java.net.InetSocketAddress;
import java.util.*;

public class MinigamePlayer {
    private ArrayList<Weapon> weapons = new ArrayList<>();

    public Player player;
    private Match match;
    private Location lastLocation;
    private ItemStack[] lastInventoryContents;
    private GameMode lastGamemode;
    private Lobby lobby;
    private int lastFoodLevel;
    private double lastHealth;
    private double lastMaxHealth;
    private float lastExp;

    /**
     * Use this method to save the player's state (location, health, etc).
     */
    public void saveCurrentState() {
        this.setLastLocation(this.getLocation());
        this.setLastHealth(this.getHealth());
        this.setLastExp(this.getExp());
        this.setLastFoodLevel(this.getFoodLevel());
        this.setLastInventoryContents(this.getInventory().getContents());
        this.setLastGamemode(this.getGameMode());
        this.setLastMaxHealth(this.getMaxHealth());
    }

    /**
     * Use this method to set the player's state back to when it was saved.
     */
    public void restoreState() {
        this.teleport(this.lastLocation, TeleportCause.PLUGIN);
        this.setHealth(this.lastHealth);
        this.setMaxHealth(this.lastMaxHealth);
        this.setExp(this.lastExp);
        this.getInventory().setContents(this.lastInventoryContents);
        this.setGameMode(this.lastGamemode);
        this.setFoodLevel(this.lastFoodLevel);
    }

    public boolean teleportToLobbyIfExists(Lobby lobby) {
        return lobby.getLocation().isPresent() && this.teleport(lobby.getLocation().get(), TeleportCause.PLUGIN);
    }

    public MinigamePlayer(Player player)
    {
        this.player = player;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public boolean inLobby() {
        return this.lobby != null;
    }

    public void addWeapon(Weapon weapon)
    {
        weapons.add(weapon);

        ItemStack itemstack = new ItemStack(weapon.getMaterial(), 1);
        ItemMeta meta = itemstack.getItemMeta();
        meta.setDisplayName(weapon.getName());
        itemstack.setItemMeta(meta);

        weapon.setPlayer(this);
        player.getInventory().addItem(itemstack);
    }

    public Weapon getWeaponInHand()
    {
        // TODO: update this
        ItemStack itemStack = player.getItemInHand();
        Weapon weapon = null;

        for (Weapon _weapon : weapons) {
            if (_weapon.getMaterial() == itemStack.getType() && _weapon.getName().equals(itemStack.getItemMeta().getDisplayName()))
                weapon = _weapon;
        }

        return weapon;
    }

    public ArrayList<Weapon> getWeapons()
    {
        return weapons;
    }

    public void respawn(Match match)
    {
        player.setHealth(20);
        player.teleport(match.getFeasibleSpawn().getLocation(), TeleportCause.PLUGIN);
    }

    public Location getLastLocation()
    {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation)
    {
        this.lastLocation = lastLocation;
    }

    public ItemStack[] getLastInventoryContents()
    {
        return lastInventoryContents;
    }

    public void setLastInventoryContents(ItemStack[] contents)
    {
        this.lastInventoryContents = contents;
    }

    public Match getMatch() {
        return match;
    }

    public boolean isInMatch()
    {
        return match != null;
    }

    public void join(Match match) {
        this.match = match;
    }

    public GameMode getLastGamemode() {
        return lastGamemode;
    }

    public void setLastGamemode(GameMode lastGamemode) {
        this.lastGamemode = lastGamemode;
    }

    public float getLastExp() {
        return lastExp;
    }

    public void setLastExp(float lastExp) {
        this.lastExp = lastExp;
    }

    public int getLastFoodLevel() {
        return lastFoodLevel;
    }

    public void setLastFoodLevel(int lastFoodLevel) {
        this.lastFoodLevel = lastFoodLevel;
    }

    public double getLastHealth() {
        return lastHealth;
    }

    public void setLastHealth(double lastHealth) {
        this.lastHealth = lastHealth;
    }

    public double getLastMaxHealth() {
        return lastMaxHealth;
    }

    public void setLastMaxHealth(double lastMaxHealth) {
        this.lastMaxHealth = lastMaxHealth;
    }

    /**
     * Gets the "friendly" name to display of this player. This may include
     * color.
     * <p>
     * Note that this name will not be displayed in game, only in chat and
     * places defined by plugins.
     *
     * @return the friendly name
     */
    public String getDisplayName() {
        return player.getDisplayName();
    }

    /**
     * Sets the "friendly" name to display of this player. This may include
     * color.
     * <p>
     * Note that this name will not be displayed in game, only in chat and
     * places defined by plugins.
     *
     * @param name The new display name.
     */
    public void setDisplayName(String name) {
        player.setDisplayName(name);
    }

    /**
     * Gets the name that is shown on the player list.
     *
     * @return the player list name
     */
    public String getPlayerListName() {
        return player.getPlayerListName();
    }

    /**
     * Sets the name that is shown on the in-game player list.
     * <p>
     * The name cannot be longer than 16 characters, but {@link ChatColor} is
     * supported.
     * <p>
     * If the value is null, the name will be identical to {@link #getName()}.
     * <p>
     * This name is case sensitive and unique, two names with different casing
     * will appear as two different people. If a player joins afterwards with
     * a name that conflicts with a player's custom list name, the joining
     * player's player list name will have a random number appended to it (1-2
     * characters long in the default implementation). If the joining player's
     * name is 15 or 16 characters long, part of the name will be truncated at
     * the end to allow the addition of the two digits.
     *
     * @param name new player list name
     * @throws IllegalArgumentException if the name is already used by someone
     *     else
     * @throws IllegalArgumentException if the length of the name is too long
     */
    public void setPlayerListName(String name) {
        player.setPlayerListName(name);
    }

    /**
     * Set the target of the player's compass.
     *
     * @param loc Location to point to
     */
    public void setCompassTarget(Location loc) {
        player.setCompassTarget(loc);
    }

    /**
     * Get the previously set compass target.
     *
     * @return location of the target
     */
    public Location getCompassTarget() {
        return player.getCompassTarget();
    }

    /**
     * Gets the socket address of this player
     *
     * @return the player's address
     */
    public InetSocketAddress getAddress() {
        return player.getAddress();
    }

    /**
     * Sends this sender a message raw
     *
     * @param message Message to be displayed
     */
    public void sendRawMessage(String message) {
        player.sendRawMessage(message);
    }

    /**
     * Kicks player with custom kick message.
     *
     * @param message kick message
     */
    public void kickPlayer(String message) {
        player.kickPlayer(message);
    }

    /**
     * Says a message (or runs a command).
     *
     * @param msg message to print
     */
    public void chat(String msg) {
        player.chat(msg);
    }

    /**
     * Makes the player perform the given command
     *
     * @param command Command to perform
     * @return true if the command was successful, otherwise false
     */
    public boolean performCommand(String command) {
        return player.performCommand(command);
    }

    /**
     * Returns if the player is in sneak mode
     *
     * @return true if player is in sneak mode
     */
    public boolean isSneaking() {
        return player.isSneaking();
    }

    /**
     * Sets the sneak mode the player
     *
     * @param sneak true if player should appear sneaking
     */
    public void setSneaking(boolean sneak) {
        player.setSneaking(sneak);
    }

    /**
     * Gets whether the player is sprinting or not.
     *
     * @return true if player is sprinting.
     */
    public boolean isSprinting() {
        return player.isSprinting();
    }

    /**
     * Sets whether the player is sprinting or not.
     *
     * @param sprinting true if the player should be sprinting
     */
    public void setSprinting(boolean sprinting) {
        player.setSprinting(sprinting);
    }

    /**
     * Saves the players current location, health, inventory, motion, and
     * other information into the username.dat file, in the world/player
     * folder
     */
    public void saveData() {
        player.saveData();
    }

    /**
     * Loads the players current location, health, inventory, motion, and
     * other information from the username.dat file, in the world/player
     * folder.
     * <p>
     * Note: This will overwrite the players current inventory, health,
     * motion, etc, with the state from the saved dat file.
     */
    public void loadData() {
        player.loadData();
    }

    /**
     * Sets whether the player is ignored as not sleeping. If everyone is
     * either sleeping or has this flag set, then time will advance to the
     * next day. If everyone has this flag set but no one is actually in bed,
     * then nothing will happen.
     *
     * @param isSleeping Whether to ignore.
     */
    public void setSleepingIgnored(boolean isSleeping) {
        player.setSleepingIgnored(isSleeping);
    }

    /**
     * Returns whether the player is sleeping ignored.
     *
     * @return Whether player is ignoring sleep.
     */
    public boolean isSleepingIgnored() {
        return player.isSleepingIgnored();
    }

    /**
     * Play a note for a player at a location. This requires a note block
     * at the particular location (as far as the client is concerned). This
     * will not work without a note block. This will not work with cake.
     *
     * @param loc The location of a note block.
     * @param instrument The instrument ID.
     * @param note The note ID.
     * @deprecated Magic value
     */
    @Deprecated
    public void playNote(Location loc, byte instrument, byte note) {
        player.playNote(loc, instrument, note);
    }

    /**
     * Play a note for a player at a location. This requires a note block
     * at the particular location (as far as the client is concerned). This
     * will not work without a note block. This will not work with cake.
     *  @param loc The location of a note block
     * @param instrument The instrument
     * @param note The note
     */
    public void playNote(Location loc, Instrument instrument, Note note) {
        player.playNote(loc, instrument, note);
    }

    /**
     * Play a sound for a player at the location.
     * <p>
     * This function will fail silently if Location or Sound are null.
     *  @param location The location to play the sound
     * @param sound The sound to play
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     */
    public void playSound(Location location, Sound sound, float volume, float pitch) {
        player.playSound(location, sound, volume, pitch);
    }

    /**
     * Play a sound for a player at the location.
     * <p>
     * This function will fail silently if Location or Sound are null. No
     * sound will be heard by the player if their client does not have the
     * respective sound for the value passed.
     *  @param location the location to play the sound
     * @param sound the internal sound name to play
     * @param volume the volume of the sound
     * @param pitch the pitch of the sound
     */
    public void playSound(Location location, String sound, float volume, float pitch) {
        player.playSound(location, sound, volume, pitch);
    }

    /**
     * Stop the specified sound from playing.
     *
     * @param sound the sound to stop
     */
    public void stopSound(Sound sound) {
        player.stopSound(sound);
    }

    /**
     * Stop the specified sound from playing.
     *
     * @param sound the sound to stop
     */
    public void stopSound(String sound) {
        player.stopSound(sound);
    }

    /**
     * Plays an effect to just this player.
     *
     * @param loc the location to play the effect at
     * @param effect the {@link Effect}
     * @param data a data bit needed for some effects
     * @deprecated Magic value
     */
    @Deprecated
    public void playEffect(Location loc, Effect effect, int data) {
        player.playEffect(loc, effect, data);
    }

    /**
     * Plays an effect to just this player.
     *  @param loc the location to play the effect at
     * @param effect the {@link Effect}
     * @param data a data bit needed for some effects
     */
    public <T> void playEffect(Location loc, Effect effect, T data) {
        player.playEffect(loc, effect, data);
    }

    /**
     * Send a block change. This fakes a block change packet for a user at a
     * certain location. This will not actually change the world in any way.
     *
     * @param loc The location of the changed block
     * @param material The new block
     * @param data The block data
     * @deprecated Magic value
     */
    @Deprecated
    public void sendBlockChange(Location loc, Material material, byte data) {
        player.sendBlockChange(loc, material, data);
    }

    /**
     * Send a chunk change. This fakes a chunk change packet for a user at a
     * certain location. The updated cuboid must be entirely within a single
     * chunk. This will not actually change the world in any way.
     * <p>
     * At least one of the dimensions of the cuboid must be even. The size of
     * the data buffer must be 2.5*sx*sy*sz and formatted in accordance with
     * the Packet51 format.
     *
     * @param loc The location of the cuboid
     * @param sx The x size of the cuboid
     * @param sy The y size of the cuboid
     * @param sz The z size of the cuboid
     * @param data The data to be sent
     * @return true if the chunk change packet was sent
     * @deprecated Magic value
     */
    @Deprecated
    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
        return player.sendChunkChange(loc, sx, sy, sz, data);
    }

    /**
     * Send a block change. This fakes a block change packet for a user at a
     * certain location. This will not actually change the world in any way.
     *
     * @param loc The location of the changed block
     * @param material The new block ID
     * @param data The block data
     * @deprecated Magic value
     */
    @Deprecated
    public void sendBlockChange(Location loc, int material, byte data) {
        player.sendBlockChange(loc, material, data);
    }

    /**
     * Send a sign change. This fakes a sign change packet for a user at
     * a certain location. This will not actually change the world in any way.
     * This method will use a sign at the location's block or a faked sign
     * sent via {@link #sendBlockChange(Location, int, byte)} or
     * {@link #sendBlockChange(Location, Material, byte)}.
     * <p>
     * If the client does not have a sign at the given location it will
     * display an error message to the user.
     *
     * @param loc the location of the sign
     * @param lines the new text on the sign or null to clear it
     * @throws IllegalArgumentException if location is null
     * @throws IllegalArgumentException if lines is non-null and has a length less than 4
     */
    public void sendSignChange(Location loc, String[] lines) throws IllegalArgumentException {
        player.sendSignChange(loc, lines);
    }

    /**
     * Render a map and send it to the player in its entirety. This may be
     * used when streaming the map in the normal manner is not desirable.
     *
     * @param map The map to be sent
     */
    public void sendMap(MapView map) {
        player.sendMap(map);
    }

    /**
     * Forces an update of the player's entire inventory.
     *
     */
    public void updateInventory() {
        player.updateInventory();
    }

    /**
     * Awards the given achievement and any parent achievements that the
     * player does not have.
     *
     * @param achievement Achievement to award
     * @throws IllegalArgumentException if achievement is null
     */
    public void awardAchievement(Achievement achievement) {
        player.awardAchievement(achievement);
    }

    /**
     * Removes the given achievement and any children achievements that the
     * player has.
     *
     * @param achievement Achievement to remove
     * @throws IllegalArgumentException if achievement is null
     */
    public void removeAchievement(Achievement achievement) {
        player.removeAchievement(achievement);
    }

    /**
     * Gets whether this player has the given achievement.
     *
     * @param achievement the achievement to check
     * @return whether the player has the achievement
     * @throws IllegalArgumentException if achievement is null
     */
    public boolean hasAchievement(Achievement achievement) {
        return player.hasAchievement(achievement);
    }

    /**
     * Increments the given statistic for this player.
     * <p>
     * This is equivalent to the following code:
     * <code>incrementStatistic(Statistic, 1)</code>
     *
     * @param statistic Statistic to increment
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if the statistic requires an
     *     additional parameter
     */
    public void incrementStatistic(Statistic statistic) throws IllegalArgumentException {
        player.incrementStatistic(statistic);
    }

    /**
     * Decrements the given statistic for this player.
     * <p>
     * This is equivalent to the following code:
     * <code>decrementStatistic(Statistic, 1)</code>
     *
     * @param statistic Statistic to decrement
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if the statistic requires an
     *     additional parameter
     */
    public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {
        player.decrementStatistic(statistic);
    }

    /**
     * Increments the given statistic for this player.
     *
     * @param statistic Statistic to increment
     * @param amount Amount to increment this statistic by
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if amount is negative
     * @throws IllegalArgumentException if the statistic requires an
     *     additional parameter
     */
    public void incrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
        player.incrementStatistic(statistic, amount);
    }

    /**
     * Decrements the given statistic for this player.
     *
     * @param statistic Statistic to decrement
     * @param amount Amount to decrement this statistic by
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if amount is negative
     * @throws IllegalArgumentException if the statistic requires an
     *     additional parameter
     */
    public void decrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
        player.decrementStatistic(statistic, amount);
    }

    /**
     * Sets the given statistic for this player.
     *
     * @param statistic Statistic to set
     * @param newValue The value to set this statistic to
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if newValue is negative
     * @throws IllegalArgumentException if the statistic requires an
     *     additional parameter
     */
    public void setStatistic(Statistic statistic, int newValue) throws IllegalArgumentException {
        player.setStatistic(statistic, newValue);
    }

    /**
     * Gets the value of the given statistic for this player.
     *
     * @param statistic Statistic to check
     * @return the value of the given statistic
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if the statistic requires an
     *     additional parameter
     */
    public int getStatistic(Statistic statistic) throws IllegalArgumentException {
        return player.getStatistic(statistic);
    }

    /**
     * Increments the given statistic for this player for the given material.
     * <p>
     * This is equivalent to the following code:
     * <code>incrementStatistic(Statistic, Material, 1)</code>
     *
     * @param statistic Statistic to increment
     * @param material Material to offset the statistic with
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if material is null
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        player.incrementStatistic(statistic, material);
    }

    /**
     * Decrements the given statistic for this player for the given material.
     * <p>
     * This is equivalent to the following code:
     * <code>decrementStatistic(Statistic, Material, 1)</code>
     *
     * @param statistic Statistic to decrement
     * @param material Material to offset the statistic with
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if material is null
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        player.decrementStatistic(statistic, material);
    }

    /**
     * Gets the value of the given statistic for this player.
     *
     * @param statistic Statistic to check
     * @param material Material offset of the statistic
     * @return the value of the given statistic
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if material is null
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        return player.getStatistic(statistic, material);
    }

    /**
     * Increments the given statistic for this player for the given material.
     *
     * @param statistic Statistic to increment
     * @param material Material to offset the statistic with
     * @param amount Amount to increment this statistic by
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if material is null
     * @throws IllegalArgumentException if amount is negative
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public void incrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
        player.incrementStatistic(statistic, material, amount);
    }

    /**
     * Decrements the given statistic for this player for the given material.
     *
     * @param statistic Statistic to decrement
     * @param material Material to offset the statistic with
     * @param amount Amount to decrement this statistic by
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if material is null
     * @throws IllegalArgumentException if amount is negative
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public void decrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
        player.decrementStatistic(statistic, material, amount);
    }

    /**
     * Sets the given statistic for this player for the given material.
     *
     * @param statistic Statistic to set
     * @param material Material to offset the statistic with
     * @param newValue The value to set this statistic to
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if material is null
     * @throws IllegalArgumentException if newValue is negative
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public void setStatistic(Statistic statistic, Material material, int newValue) throws IllegalArgumentException {
        player.setStatistic(statistic, material, newValue);
    }

    /**
     * Increments the given statistic for this player for the given entity.
     * <p>
     * This is equivalent to the following code:
     * <code>incrementStatistic(Statistic, EntityType, 1)</code>
     *
     * @param statistic Statistic to increment
     * @param entityType EntityType to offset the statistic with
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if entityType is null
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        player.incrementStatistic(statistic, entityType);
    }

    /**
     * Decrements the given statistic for this player for the given entity.
     * <p>
     * This is equivalent to the following code:
     * <code>decrementStatistic(Statistic, EntityType, 1)</code>
     *
     * @param statistic Statistic to decrement
     * @param entityType EntityType to offset the statistic with
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if entityType is null
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        player.decrementStatistic(statistic, entityType);
    }

    /**
     * Gets the value of the given statistic for this player.
     *
     * @param statistic Statistic to check
     * @param entityType EntityType offset of the statistic
     * @return the value of the given statistic
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if entityType is null
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        return player.getStatistic(statistic, entityType);
    }

    /**
     * Increments the given statistic for this player for the given entity.
     *
     * @param statistic Statistic to increment
     * @param entityType EntityType to offset the statistic with
     * @param amount Amount to increment this statistic by
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if entityType is null
     * @throws IllegalArgumentException if amount is negative
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) throws IllegalArgumentException {
        player.incrementStatistic(statistic, entityType, amount);
    }

    /**
     * Decrements the given statistic for this player for the given entity.
     *
     * @param statistic Statistic to decrement
     * @param entityType EntityType to offset the statistic with
     * @param amount Amount to decrement this statistic by
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if entityType is null
     * @throws IllegalArgumentException if amount is negative
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        player.decrementStatistic(statistic, entityType, amount);
    }

    /**
     * Sets the given statistic for this player for the given entity.
     *
     * @param statistic Statistic to set
     * @param entityType EntityType to offset the statistic with
     * @param newValue The value to set this statistic to
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if entityType is null
     * @throws IllegalArgumentException if newValue is negative
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
        player.setStatistic(statistic, entityType, newValue);
    }

    /**
     * Sets the current time on the player's client. When relative is true the
     * player's time will be kept synchronized to its world time with the
     * specified offset.
     * <p>
     * When using non relative time the player's time will stay fixed at the
     * specified time parameter. It's up to the caller to continue updating
     * the player's time. To restore player time to normal use
     * resetPlayerTime().
     *  @param time The current player's perceived time or the player's time
     *     offset from the server time.
     * @param relative When true the player time is kept relative to its world
     */
    public void setPlayerTime(long time, boolean relative) {
        player.setPlayerTime(time, relative);
    }

    /**
     * Returns the player's current timestamp.
     *
     * @return The player's time
     */
    public long getPlayerTime() {
        return player.getPlayerTime();
    }

    /**
     * Returns the player's current time offset relative to server time, or
     * the current player's fixed time if the player's time is absolute.
     *
     * @return The player's time
     */
    public long getPlayerTimeOffset() {
        return player.getPlayerTimeOffset();
    }

    /**
     * Returns true if the player's time is relative to the server time,
     * otherwise the player's time is absolute and will not change its current
     * time unless done so with setPlayerTime().
     *
     * @return true if the player's time is relative to the server time.
     */
    public boolean isPlayerTimeRelative() {
        return player.isPlayerTimeRelative();
    }

    /**
     * Restores the normal condition where the player's time is synchronized
     * with the server time.
     * <p>
     * Equivalent to calling setPlayerTime(0, true).
     */
    public void resetPlayerTime() {
        player.resetPlayerTime();
    }

    /**
     * Sets the type of weather the player will see.  When used, the weather
     * status of the player is locked until {@link #resetPlayerWeather()} is
     * used.
     *
     * @param type The WeatherType enum type the player should experience
     */
    public void setPlayerWeather(WeatherType type) {
        player.setPlayerWeather(type);
    }

    /**
     * Returns the type of weather the player is currently experiencing.
     *
     * @return The WeatherType that the player is currently experiencing or
     *     null if player is seeing server weather.
     */
    public WeatherType getPlayerWeather() {
        return player.getPlayerWeather();
    }

    /**
     * Restores the normal condition where the player's weather is controlled
     * by server conditions.
     */
    public void resetPlayerWeather() {
        player.resetPlayerWeather();
    }

    /**
     * Gives the player the amount of experience specified.
     *
     * @param amount Exp amount to give
     */
    public void giveExp(int amount) {
        player.giveExp(amount);
    }

    /**
     * Gives the player the amount of experience levels specified. Levels can
     * be taken by specifying a negative amount.
     *
     * @param amount amount of experience levels to give or take
     */
    public void giveExpLevels(int amount) {
        player.giveExpLevels(amount);
    }

    /**
     * Gets the players current experience points towards the next level.
     * <p>
     * This is a percentage value. 0 is "no progress" and 1 is "next level".
     *
     * @return Current experience points
     */
    public float getExp() {
        return player.getExp();
    }

    /**
     * Sets the players current experience points towards the next level
     * <p>
     * This is a percentage value. 0 is "no progress" and 1 is "next level".
     *
     * @param exp New experience points
     */
    public void setExp(float exp) {
        player.setExp(exp);
    }

    /**
     * Gets the players current experience level
     *
     * @return Current experience level
     */
    public int getLevel() {
        return player.getLevel();
    }

    /**
     * Sets the players current experience level
     *
     * @param level New experience level
     */
    public void setLevel(int level) {
        player.setLevel(level);
    }

    /**
     * Gets the players total experience points
     *
     * @return Current total experience points
     */
    public int getTotalExperience() {
        return player.getTotalExperience();
    }

    /**
     * Sets the players current experience level
     *
     * @param exp New experience level
     */
    public void setTotalExperience(int exp) {
        player.setTotalExperience(exp);
    }

    /**
     * Gets the players current exhaustion level.
     * <p>
     * Exhaustion controls how fast the food level drops. While you have a
     * certain amount of exhaustion, your saturation will drop to zero, and
     * then your food will drop to zero.
     *
     * @return Exhaustion level
     */
    public float getExhaustion() {
        return player.getExhaustion();
    }

    /**
     * Sets the players current exhaustion level
     *
     * @param value Exhaustion level
     */
    public void setExhaustion(float value) {
        player.setExhaustion(value);
    }

    /**
     * Gets the players current saturation level.
     * <p>
     * Saturation is a buffer for food level. Your food level will not drop if
     * you are saturated {@literal >} 0.
     *
     * @return Saturation level
     */
    public float getSaturation() {
        return player.getSaturation();
    }

    /**
     * Sets the players current saturation level
     *
     * @param value Saturation level
     */
    public void setSaturation(float value) {
        player.setSaturation(value);
    }

    /**
     * Gets the players current food level
     *
     * @return Food level
     */
    public int getFoodLevel() {
        return player.getFoodLevel();
    }

    /**
     * Sets the players current food level
     *
     * @param value New food level
     */
    public void setFoodLevel(int value) {
        player.setFoodLevel(value);
    }

    /**
     * Gets the Location where the player will spawn at their bed, null if
     * they have not slept in one or their current bed spawn is invalid.
     *
     * @return Bed Spawn Location if bed exists, otherwise null.
     */
    public Location getBedSpawnLocation() {
        return player.getBedSpawnLocation();
    }

    /**
     * Sets the Location where the player will spawn at their bed.
     *
     * @param location where to set the respawn location
     */
    public void setBedSpawnLocation(Location location) {
        player.setBedSpawnLocation(location);
    }

    /**
     * Sets the Location where the player will spawn at their bed.
     *  @param location where to set the respawn location
     * @param force whether to forcefully set the respawn location even if a
     */
    public void setBedSpawnLocation(Location location, boolean force) {
        player.setBedSpawnLocation(location, force);
    }

    /**
     * Determines if the Player is allowed to fly via jump key double-tap like
     * in creative mode.
     *
     * @return True if the player is allowed to fly.
     */
    public boolean getAllowFlight() {
        return player.getAllowFlight();
    }

    /**
     * Sets if the Player is allowed to fly via jump key double-tap like in
     * creative mode.
     *
     * @param flight If flight should be allowed.
     */
    public void setAllowFlight(boolean flight) {
        player.setAllowFlight(flight);
    }

    /**
     * Hides a player from this player
     *
     * @param player Player to hide
     */
    public void hidePlayer(Player player) {
        this.player.hidePlayer(player);
    }

    /**
     * Allows this player to see a player that was previously hidden
     *
     * @param player Player to show
     */
    public void showPlayer(Player player) {
        this.player.showPlayer(player);
    }

    /**
     * Checks to see if a player has been hidden from this player
     *
     * @param player Player to check
     * @return True if the provided player is not being hidden from this
     *     player
     */
    public boolean canSee(Player player) {
        return this.player.canSee(player);
    }

    /**
     * Checks to see if this player is currently standing on a block. This
     * information may not be reliable, as it is a state provided by the
     * client, and may therefore not be accurate.
     *
     * @return True if the player standing on a solid block, else false.
     * @deprecated Inconsistent with {@link
     *     Entity#isOnGround()}
     */
    @Deprecated
    public boolean isOnGround() {
        return player.isOnGround();
    }

    /**
     * Checks to see if this player is currently flying or not.
     *
     * @return True if the player is flying, else false.
     */
    public boolean isFlying() {
        return player.isFlying();
    }

    /**
     * Makes this player start or stop flying.
     *
     * @param value True to fly.
     */
    public void setFlying(boolean value) {
        player.setFlying(value);
    }

    /**
     * Sets the speed at which a client will fly. Negative values indicate
     * reverse directions.
     *
     * @param value The new speed, from -1 to 1.
     * @throws IllegalArgumentException If new speed is less than -1 or
     *     greater than 1
     */
    public void setFlySpeed(float value) throws IllegalArgumentException {
        player.setFlySpeed(value);
    }

    /**
     * Sets the speed at which a client will walk. Negative values indicate
     * reverse directions.
     *
     * @param value The new speed, from -1 to 1.
     * @throws IllegalArgumentException If new speed is less than -1 or
     *     greater than 1
     */
    public void setWalkSpeed(float value) throws IllegalArgumentException {
        player.setWalkSpeed(value);
    }

    /**
     * Gets the current allowed speed that a client can fly.
     *
     * @return The current allowed speed, from -1 to 1
     */
    public float getFlySpeed() {
        return player.getFlySpeed();
    }

    /**
     * Gets the current allowed speed that a client can walk.
     *
     * @return The current allowed speed, from -1 to 1
     */
    public float getWalkSpeed() {
        return player.getWalkSpeed();
    }

    /**
     * Request that the player's client download and switch texture packs.
     * <p>
     * The player's client will download the new texture pack asynchronously
     * in the background, and will automatically switch to it once the
     * download is complete. If the client has downloaded and cached the same
     * texture pack in the past, it will perform a quick timestamp check over
     * the network to determine if the texture pack has changed and needs to
     * be downloaded again. When this request is sent for the very first time
     * from a given server, the client will first display a confirmation GUI
     * to the player before proceeding with the download.
     * <p>
     * Notes:
     * <ul>
     * <li>Players can disable server textures on their client, in which
     *     case this method will have no affect on them.
     * <li>There is no concept of resetting texture packs back to default
     *     within Minecraft, so players will have to relog to do so.
     * </ul>
     *
     * @param url The URL from which the client will download the texture
     *     pack. The string must contain only US-ASCII characters and should
     *     be encoded as per RFC 1738.
     * @throws IllegalArgumentException Thrown if the URL is null.
     * @throws IllegalArgumentException Thrown if the URL is too long.
     * @deprecated Minecraft no longer uses textures packs. Instead you
     *     should use {@link #setResourcePack(String)}.
     */
    @Deprecated
    public void setTexturePack(String url) {
        player.setTexturePack(url);
    }

    /**
     * Request that the player's client download and switch resource packs.
     * <p>
     * The player's client will download the new resource pack asynchronously
     * in the background, and will automatically switch to it once the
     * download is complete. If the client has downloaded and cached the same
     * resource pack in the past, it will perform a quick timestamp check
     * over the network to determine if the resource pack has changed and
     * needs to be downloaded again. When this request is sent for the very
     * first time from a given server, the client will first display a
     * confirmation GUI to the player before proceeding with the download.
     * <p>
     * Notes:
     * <ul>
     * <li>Players can disable server resources on their client, in which
     *     case this method will have no affect on them.
     * <li>There is no concept of resetting resource packs back to default
     *     within Minecraft, so players will have to relog to do so.
     * </ul>
     *
     * @param url The URL from which the client will download the resource
     *     pack. The string must contain only US-ASCII characters and should
     *     be encoded as per RFC 1738.
     * @throws IllegalArgumentException Thrown if the URL is null.
     * @throws IllegalArgumentException Thrown if the URL is too long. The
     *     length restriction is an implementation specific arbitrary value.
     */
    public void setResourcePack(String url) {
        player.setResourcePack(url);
    }

    /**
     * Gets the Scoreboard displayed to this player
     *
     * @return The current scoreboard seen by this player
     */
    public Scoreboard getScoreboard() {
        return player.getScoreboard();
    }

    /**
     * Sets the player's visible Scoreboard.
     *
     * @param scoreboard New Scoreboard for the player
     * @throws IllegalArgumentException if scoreboard is null
     * @throws IllegalArgumentException if scoreboard was not created by the
     *     {@link ScoreboardManager scoreboard manager}
     * @throws IllegalStateException if this is a player that is not logged
     *     yet or has logged out
     */
    public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
        player.setScoreboard(scoreboard);
    }

    /**
     * Gets if the client is displayed a 'scaled' health, that is, health on a
     * scale from 0-{@link #getHealthScale()}.
     *
     * @return if client health display is scaled
     * @see Player#setHealthScaled(boolean)
     */
    public boolean isHealthScaled() {
        return player.isHealthScaled();
    }

    /**
     * Sets if the client is displayed a 'scaled' health, that is, health on a
     * scale from 0-{@link #getHealthScale()}.
     * <p>
     * Displayed health follows a simple formula <code>displayedHealth =
     * getHealth() / getMaxHealth() * getHealthScale()</code>.
     *
     * @param scale if the client health display is scaled
     */
    public void setHealthScaled(boolean scale) {
        player.setHealthScaled(scale);
    }

    /**
     * Sets the number to scale health to for the client; this will also
     * {@link #setHealthScaled(boolean) setHealthScaled(true)}.
     * <p>
     * Displayed health follows a simple formula <code>displayedHealth =
     * getHealth() / getMaxHealth() * getHealthScale()</code>.
     *
     * @param scale the number to scale health to
     * @throws IllegalArgumentException if scale is &lt;0
     * @throws IllegalArgumentException if scale is {@link Double#NaN}
     * @throws IllegalArgumentException if scale is too high
     */
    public void setHealthScale(double scale) throws IllegalArgumentException {
        player.setHealthScale(scale);
    }

    /**
     * Gets the number that health is scaled to for the client.
     *
     * @return the number that health would be scaled to for the client if
     *     HealthScaling is set to true
     * @see Player#setHealthScale(double)
     * @see Player#setHealthScaled(boolean)
     */
    public double getHealthScale() {
        return player.getHealthScale();
    }

    /**
     * Gets the entity which is followed by the camera when in
     * {@link GameMode#SPECTATOR}.
     *
     * @return the followed entity, or null if not in spectator mode or not
     * following a specific entity.
     */
    public Entity getSpectatorTarget() {
        return player.getSpectatorTarget();
    }

    /**
     * Sets the entity which is followed by the camera when in
     * {@link GameMode#SPECTATOR}.
     *
     * @param entity the entity to follow or null to reset
     * @throws IllegalStateException if the player is not in
     * {@link GameMode#SPECTATOR}
     */
    public void setSpectatorTarget(Entity entity) {
        player.setSpectatorTarget(entity);
    }

    /**
     * Sends a title and a subtitle message to the player. If either of these
     * values are null, they will not be sent and the display will remain
     * unchanged. If they are empty strings, the display will be updated as
     * such. If the strings contain a new line, only the first line will be
     * sent.
     *
     * @param title Title text
     * @param subtitle Subtitle text
     * @deprecated API subject to change
     */
    @Deprecated
    public void sendTitle(String title, String subtitle) {
        player.sendTitle(title, subtitle);
    }

    /**
     * Resets the title displayed to the player.
     * @deprecated API subject to change.
     */
    @Deprecated
    public void resetTitle() {
        player.resetTitle();
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location.
     *  @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     */
    public void spawnParticle(Particle particle, Location location, int count) {
        player.spawnParticle(particle, location, count);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location.
     *  @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     */
    public void spawnParticle(Particle particle, double x, double y, double z, int count) {
        player.spawnParticle(particle, x, y, z, count);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location.
     *  @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    public <T> void spawnParticle(Particle particle, Location location, int count, T data) {
        player.spawnParticle(particle, location, count, data);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location.
     *  @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {
        player.spawnParticle(particle, x, y, z, count, data);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *  @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     */
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *  @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     */
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
        player.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *  @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {
        player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, data);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *  @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {
        player.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, data);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *  @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param extra the extra data for this particle, depends on the
     */
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *  @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param extra the extra data for this particle, depends on the
     */
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        player.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *  @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param extra the extra data for this particle, depends on the
     *              particle used (normally speed)
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, data);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *  @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param extra the extra data for this particle, depends on the
     *              particle used (normally speed)
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        player.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
    }

    public Player.Spigot spigot() {
        return player.spigot();
    }

    /**
     * Returns the name of this player
     *
     * @return Player name
     */
    public String getName() {
        return player.getName();
    }

    /**
     * Get the player's inventory.
     *
     * @return The inventory of the player, this also contains the armor
     *     slots.
     */
    public PlayerInventory getInventory() {
        return player.getInventory();
    }

    /**
     * Get the player's EnderChest inventory
     *
     * @return The EnderChest of the player
     */
    public Inventory getEnderChest() {
        return player.getEnderChest();
    }

    /**
     * Gets the player's selected main hand
     *
     * @return the players main hand
     */
    public MainHand getMainHand() {
        return player.getMainHand();
    }

    /**
     * If the player currently has an inventory window open, this method will
     * set a property of that window, such as the state of a progress bar.
     *
     * @param prop The property.
     * @param value The value to set the property to.
     * @return True if the property was successfully set.
     */
    public boolean setWindowProperty(InventoryView.Property prop, int value) {
        return player.setWindowProperty(prop, value);
    }

    /**
     * Gets the inventory view the player is currently viewing. If they do not
     * have an inventory window open, it returns their internal crafting view.
     *
     * @return The inventory view.
     */
    public InventoryView getOpenInventory() {
        return player.getOpenInventory();
    }

    /**
     * Opens an inventory window with the specified inventory on the top and
     * the player's inventory on the bottom.
     *
     * @param inventory The inventory to open
     * @return The newly opened inventory view
     */
    public InventoryView openInventory(Inventory inventory) {
        return player.openInventory(inventory);
    }

    /**
     * Opens an empty workbench inventory window with the player's inventory
     * on the bottom.
     *
     * @param location The location to attach it to. If null, the player's
     *     location is used.
     * @param force If false, and there is no workbench block at the location,
     *     no inventory will be opened and null will be returned.
     * @return The newly opened inventory view, or null if it could not be
     *     opened.
     */
    public InventoryView openWorkbench(Location location, boolean force) {
        return player.openWorkbench(location, force);
    }

    /**
     * Opens an empty enchanting inventory window with the player's inventory
     * on the bottom.
     *
     * @param location The location to attach it to. If null, the player's
     *     location is used.
     * @param force If false, and there is no enchanting table at the
     *     location, no inventory will be opened and null will be returned.
     * @return The newly opened inventory view, or null if it could not be
     *     opened.
     */
    public InventoryView openEnchanting(Location location, boolean force) {
        return player.openEnchanting(location, force);
    }

    /**
     * Opens an inventory window to the specified inventory view.
     *
     * @param inventory The view to open
     */
    public void openInventory(InventoryView inventory) {
        player.openInventory(inventory);
    }

    /**
     * Starts a trade between the player and the villager.
     *
     * Note that only one player may trade with a villager at once. You must use
     * the force parameter for this.
     *
     * @param trader The merchant to trade with. Cannot be null.
     * @param force whether to force the trade even if another player is trading
     * @return The newly opened inventory view, or null if it could not be
     * opened.
     */
    public InventoryView openMerchant(Villager trader, boolean force) {
        return player.openMerchant(trader, force);
    }

    /**
     * Force-closes the currently open inventory view for this player, if any.
     */
    public void closeInventory() {
        player.closeInventory();
    }

    /**
     * Returns the ItemStack currently in your hand, can be empty.
     *
     * @return The ItemStack of the item you are currently holding.
     * @deprecated Humans may now dual wield in their off hand, use explicit
     * methods in {@link PlayerInventory}.
     */
    @Deprecated
    public ItemStack getItemInHand() {
        return player.getItemInHand();
    }

    /**
     * Sets the item to the given ItemStack, this will replace whatever the
     * user was holding.
     *
     * @param item The ItemStack which will end up in the hand
     * @deprecated Humans may now dual wield in their off hand, use explicit
     * methods in {@link PlayerInventory}.
     */
    @Deprecated
    public void setItemInHand(ItemStack item) {
        player.setItemInHand(item);
    }

    /**
     * Returns the ItemStack currently on your cursor, can be empty. Will
     * always be empty if the player currently has no open window.
     *
     * @return The ItemStack of the item you are currently moving around.
     */
    public ItemStack getItemOnCursor() {
        return player.getItemOnCursor();
    }

    /**
     * Sets the item to the given ItemStack, this will replace whatever the
     * user was moving. Will always be empty if the player currently has no
     * open window.
     *
     * @param item The ItemStack which will end up in the hand
     */
    public void setItemOnCursor(ItemStack item) {
        player.setItemOnCursor(item);
    }

    /**
     * Returns whether this player is slumbering.
     *
     * @return slumber state
     */
    public boolean isSleeping() {
        return player.isSleeping();
    }

    /**
     * Get the sleep ticks of the player. This value may be capped.
     *
     * @return slumber ticks
     */
    public int getSleepTicks() {
        return player.getSleepTicks();
    }

    /**
     * Gets this human's current {@link GameMode}
     *
     * @return Current game mode
     */
    public GameMode getGameMode() {
        return player.getGameMode();
    }

    /**
     * Sets this human's current {@link GameMode}
     *
     * @param mode New game mode
     */
    public void setGameMode(GameMode mode) {
        player.setGameMode(mode);
    }

    /**
     * Check if the player is currently blocking (ie with a sword).
     *
     * @return Whether they are blocking.
     */
    public boolean isBlocking() {
        return player.isBlocking();
    }

    /**
     * Get the total amount of experience required for the player to level
     *
     * @return Experience required to level up
     */
    public int getExpToLevel() {
        return player.getExpToLevel();
    }

    /**
     * Gets the height of the living entity's eyes above its Location.
     *
     * @return height of the living entity's eyes above its location
     */
    public double getEyeHeight() {
        return player.getEyeHeight();
    }

    /**
     * Gets the height of the living entity's eyes above its Location.
     *
     * @param ignoreSneaking if set to true, the effects of sneaking will be
     *     ignored
     * @return height of the living entity's eyes above its location
     */
    public double getEyeHeight(boolean ignoreSneaking) {
        return player.getEyeHeight(ignoreSneaking);
    }

    /**
     * Get a Location detailing the current eye position of the living entity.
     *
     * @return a location at the eyes of the living entity
     */
    public Location getEyeLocation() {
        return player.getEyeLocation();
    }

    /**
     * Gets all blocks along the living entity's line of sight.
     * <p>
     * This list contains all blocks from the living entity's eye position to
     * target inclusive.
     *
     * @param transparent HashSet containing all transparent block IDs (set to
     *     null for only air)
     * @param maxDistance this is the maximum distance to scan (may be limited
     *     by server by at least 100 blocks, no less)
     * @return list containing all blocks along the living entity's line of
     *     sight
     * @deprecated Magic value
     */
    @Deprecated
    public List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance) {
        return player.getLineOfSight(transparent, maxDistance);
    }

    /**
     * Gets all blocks along the living entity's line of sight.
     * <p>
     * This list contains all blocks from the living entity's eye position to
     * target inclusive.
     *
     * @param transparent HashSet containing all transparent block Materials (set to
     *     null for only air)
     * @param maxDistance this is the maximum distance to scan (may be limited
     *     by server by at least 100 blocks, no less)
     * @return list containing all blocks along the living entity's line of
     *     sight
     */
    public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance) {
        return player.getLineOfSight(transparent, maxDistance);
    }

    /**
     * Gets the block that the living entity has targeted.
     *
     * @param transparent HashSet containing all transparent block IDs (set to
     *     null for only air)
     * @param maxDistance this is the maximum distance to scan (may be limited
     *     by server by at least 100 blocks, no less)
     * @return block that the living entity has targeted
     * @deprecated Magic value
     */
    @Deprecated
    public Block getTargetBlock(HashSet<Byte> transparent, int maxDistance) {
        return player.getTargetBlock(transparent, maxDistance);
    }

    /**
     * Gets the block that the living entity has targeted.
     *
     * @param transparent HashSet containing all transparent block Materials (set to
     *     null for only air)
     * @param maxDistance this is the maximum distance to scan (may be limited
     *     by server by at least 100 blocks, no less)
     * @return block that the living entity has targeted
     */
    public Block getTargetBlock(Set<Material> transparent, int maxDistance) {
        return player.getTargetBlock(transparent, maxDistance);
    }

    /**
     * Gets the last two blocks along the living entity's line of sight.
     * <p>
     * The target block will be the last block in the list.
     *
     * @param transparent HashSet containing all transparent block IDs (set to
     *     null for only air)
     * @param maxDistance this is the maximum distance to scan. This may be
     *     further limited by the server, but never to less than 100 blocks
     * @return list containing the last 2 blocks along the living entity's
     *     line of sight
     * @deprecated Magic value
     */
    @Deprecated
    public List<Block> getLastTwoTargetBlocks(HashSet<Byte> transparent, int maxDistance) {
        return player.getLastTwoTargetBlocks(transparent, maxDistance);
    }

    /**
     * Gets the last two blocks along the living entity's line of sight.
     * <p>
     * The target block will be the last block in the list.
     *
     * @param transparent HashSet containing all transparent block Materials (set to
     *     null for only air)
     * @param maxDistance this is the maximum distance to scan. This may be
     *     further limited by the server, but never to less than 100 blocks
     * @return list containing the last 2 blocks along the living entity's
     *     line of sight
     */
    public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {
        return player.getLastTwoTargetBlocks(transparent, maxDistance);
    }

    /**
     * Returns the amount of air that the living entity has remaining, in
     * ticks.
     *
     * @return amount of air remaining
     */
    public int getRemainingAir() {
        return player.getRemainingAir();
    }

    /**
     * Sets the amount of air that the living entity has remaining, in ticks.
     *
     * @param ticks amount of air remaining
     */
    public void setRemainingAir(int ticks) {
        player.setRemainingAir(ticks);
    }

    /**
     * Returns the maximum amount of air the living entity can have, in ticks.
     *
     * @return maximum amount of air
     */
    public int getMaximumAir() {
        return player.getMaximumAir();
    }

    /**
     * Sets the maximum amount of air the living entity can have, in ticks.
     *
     * @param ticks maximum amount of air
     */
    public void setMaximumAir(int ticks) {
        player.setMaximumAir(ticks);
    }

    /**
     * Returns the living entity's current maximum no damage ticks.
     * <p>
     * This is the maximum duration in which the living entity will not take
     * damage.
     *
     * @return maximum no damage ticks
     */
    public int getMaximumNoDamageTicks() {
        return player.getMaximumNoDamageTicks();
    }

    /**
     * Sets the living entity's current maximum no damage ticks.
     *
     * @param ticks maximum amount of no damage ticks
     */
    public void setMaximumNoDamageTicks(int ticks) {
        player.setMaximumNoDamageTicks(ticks);
    }

    /**
     * Returns the living entity's last damage taken in the current no damage
     * ticks time.
     * <p>
     * Only damage higher than this amount will further damage the living
     * entity.
     *
     * @return damage taken since the last no damage ticks time period
     */
    public double getLastDamage() {
        return player.getLastDamage();
    }

    /**
     * This method exists for legacy reasons to provide backwards
     * compatibility. It will not exist at runtime and should not be used
     * under any circumstances.
     *
     * @return damage taken since the last no damage ticks time period
     */
    @Deprecated
    public int _INVALID_getLastDamage() {
        return player._INVALID_getLastDamage();
    }

    /**
     * Sets the damage dealt within the current no damage ticks time period.
     *
     * @param damage amount of damage
     */
    public void setLastDamage(double damage) {
        player.setLastDamage(damage);
    }

    /**
     * This method exists for legacy reasons to provide backwards
     * compatibility. It will not exist at runtime and should not be used
     * under any circumstances.
     *
     * @param damage amount of damage
     */
    @Deprecated
    public void _INVALID_setLastDamage(int damage) {
        player._INVALID_setLastDamage(damage);
    }

    /**
     * Returns the living entity's current no damage ticks.
     *
     * @return amount of no damage ticks
     */
    public int getNoDamageTicks() {
        return player.getNoDamageTicks();
    }

    /**
     * Sets the living entity's current no damage ticks.
     *
     * @param ticks amount of no damage ticks
     */
    public void setNoDamageTicks(int ticks) {
        player.setNoDamageTicks(ticks);
    }

    /**
     * Gets the player identified as the killer of the living entity.
     * <p>
     * May be null.
     *
     * @return killer player, or null if none found
     */
    public Player getKiller() {
        return player.getKiller();
    }

    /**
     * Adds the given {@link PotionEffect} to the living entity.
     * <p>
     * Only one potion effect can be present for a given {@link
     * PotionEffectType}.
     *
     * @param effect PotionEffect to be added
     * @return whether the effect could be added
     */
    public boolean addPotionEffect(PotionEffect effect) {
        return player.addPotionEffect(effect);
    }

    /**
     * Adds the given {@link PotionEffect} to the living entity.
     * <p>
     * Only one potion effect can be present for a given {@link
     * PotionEffectType}.
     *
     * @param effect PotionEffect to be added
     * @param force whether conflicting effects should be removed
     * @return whether the effect could be added
     */
    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        return player.addPotionEffect(effect, force);
    }

    /**
     * Attempts to add all of the given {@link PotionEffect} to the living
     * entity.
     *
     * @param effects the effects to add
     * @return whether all of the effects could be added
     */
    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        return player.addPotionEffects(effects);
    }

    /**
     * Returns whether the living entity already has an existing effect of
     * the given {@link PotionEffectType} applied to it.
     *
     * @param type the potion type to check
     * @return whether the living entity has this potion effect active on them
     */
    public boolean hasPotionEffect(PotionEffectType type) {
        return player.hasPotionEffect(type);
    }

    /**
     * Removes any effects present of the given {@link PotionEffectType}.
     *
     * @param type the potion type to remove
     */
    public void removePotionEffect(PotionEffectType type) {
        player.removePotionEffect(type);
    }

    /**
     * Returns all currently active {@link PotionEffect}s on the living
     * entity.
     *
     * @return a collection of {@link PotionEffect}s
     */
    public Collection<PotionEffect> getActivePotionEffects() {
        return player.getActivePotionEffects();
    }

    /**
     * Checks whether the living entity has block line of sight to another.
     * <p>
     * This uses the same algorithm that hostile mobs use to find the closest
     * player.
     *
     * @param other the entity to determine line of sight to
     * @return true if there is a line of sight, false if not
     */
    public boolean hasLineOfSight(Entity other) {
        return player.hasLineOfSight(other);
    }

    /**
     * Returns if the living entity despawns when away from players or not.
     * <p>
     * By default, animals are not removed while other mobs are.
     *
     * @return true if the living entity is removed when away from players
     */
    public boolean getRemoveWhenFarAway() {
        return player.getRemoveWhenFarAway();
    }

    /**
     * Sets whether or not the living entity despawns when away from players
     * or not.
     *
     * @param remove the removal status
     */
    public void setRemoveWhenFarAway(boolean remove) {
        player.setRemoveWhenFarAway(remove);
    }

    /**
     * Gets the inventory with the equipment worn by the living entity.
     *
     * @return the living entity's inventory
     */
    public EntityEquipment getEquipment() {
        return player.getEquipment();
    }

    /**
     * Sets whether or not the living entity can pick up items.
     *
     * @param pickup whether or not the living entity can pick up items
     */
    public void setCanPickupItems(boolean pickup) {
        player.setCanPickupItems(pickup);
    }

    /**
     * Gets if the living entity can pick up items.
     *
     * @return whether or not the living entity can pick up items
     */
    public boolean getCanPickupItems() {
        return player.getCanPickupItems();
    }

    /**
     * Returns whether the entity is currently leashed.
     *
     * @return whether the entity is leashed
     */
    public boolean isLeashed() {
        return player.isLeashed();
    }

    /**
     * Gets the entity that is currently leading this entity.
     *
     * @return the entity holding the leash
     * @throws IllegalStateException if not currently leashed
     */
    public Entity getLeashHolder() throws IllegalStateException {
        return player.getLeashHolder();
    }

    /**
     * Sets the leash on this entity to be held by the supplied entity.
     * <p>
     * This method has no effect on EnderDragons, Withers, Players, or Bats.
     * Non-living entities excluding leashes will not persist as leash
     * holders.
     *
     * @param holder the entity to leash this entity to
     * @return whether the operation was successful
     */
    public boolean setLeashHolder(Entity holder) {
        return player.setLeashHolder(holder);
    }

    /**
     * Checks to see if an entity is gliding, such as using an Elytra.
     * @return True if this entity is gliding.
     */
    public boolean isGliding() {
        return player.isGliding();
    }

    /**
     * Makes entity start or stop gliding. This will work even if an Elytra
     * is not equipped, but will be reverted by the server immediately after
     * unless an event-cancelling mechanism is put in place.
     * @param gliding True if the entity is gliding.
     */
    public void setGliding(boolean gliding) {
        player.setGliding(gliding);
    }

    /**
     * Sets whether an entity will have AI.
     *
     * @param ai whether the mob will have AI or not.
     */
    public void setAI(boolean ai) {
        player.setAI(ai);
    }

    /**
     * Checks whether an entity has AI.
     *
     * @return true if the entity has AI, otherwise false.
     */
    public boolean hasAI() {
        return player.hasAI();
    }

    /**
     * Set if this entity will be subject to collisions other entities.
     * <p>
     * Note that collisions are bidirectional, so this method would need to be
     * set to false on both the collidee and the collidant to ensure no
     * collisions take place.
     *
     * @param collidable collision status
     */
    public void setCollidable(boolean collidable) {
        player.setCollidable(collidable);
    }

    /**
     * Gets if this entity is subject to collisions with other entities.
     * <p>
     * Please note that this method returns only the custom collidable state,
     * not whether the entity is non-collidable for other reasons such as being
     * dead.
     *
     * @return collision status
     */
    public boolean isCollidable() {
        return player.isCollidable();
    }

    /**
     * Gets the specified attribute instance from the object. This instance will
     * be backed directly to the object and any changes will be visible at once.
     *
     * @param attribute the attribute to get
     * @return the attribute instance or null if not applicable to this object
     */
    public AttributeInstance getAttribute(Attribute attribute) {
        return player.getAttribute(attribute);
    }

    /**
     * Gets the entity's current position
     *
     * @return a new copy of Location containing the position of this entity
     */
    public Location getLocation() {
        return player.getLocation();
    }

    /**
     * Stores the entity's current position in the provided Location object.
     * <p>
     * If the provided Location is null this method does nothing and returns
     * null.
     *
     * @param loc the location to copy into
     * @return The Location object provided or null
     */
    public Location getLocation(Location loc) {
        return player.getLocation(loc);
    }

    /**
     * Sets this entity's velocity
     *
     * @param velocity New velocity to travel with
     */
    public void setVelocity(Vector velocity) {
        player.setVelocity(velocity);
    }

    /**
     * Gets this entity's current velocity
     *
     * @return Current travelling velocity of this entity
     */
    public Vector getVelocity() {
        return player.getVelocity();
    }

    /**
     * Gets the current world this entity resides in
     *
     * @return World
     */
    public World getWorld() {
        return player.getWorld();
    }

    /**
     * Teleports this entity to the given location. If this entity is riding a
     * vehicle, it will be dismounted prior to teleportation.
     *
     * @param location New location to teleport this entity to
     * @return <code>true</code> if the teleport was successful
     */
    public boolean teleport(Location location) {
        return player.teleport(location);
    }

    /**
     * Teleports this entity to the given location. If this entity is riding a
     * vehicle, it will be dismounted prior to teleportation.
     *
     * @param location New location to teleport this entity to
     * @param cause The cause of this teleportation
     * @return <code>true</code> if the teleport was successful
     */
    public boolean teleport(Location location, TeleportCause cause) {
        return player.teleport(location, cause);
    }

    /**
     * Teleports this entity to the target Entity. If this entity is riding a
     * vehicle, it will be dismounted prior to teleportation.
     *
     * @param destination Entity to teleport this entity to
     * @return <code>true</code> if the teleport was successful
     */
    public boolean teleport(Entity destination) {
        return player.teleport(destination);
    }

    /**
     * Teleports this entity to the target Entity. If this entity is riding a
     * vehicle, it will be dismounted prior to teleportation.
     *
     * @param destination Entity to teleport this entity to
     * @param cause The cause of this teleportation
     * @return <code>true</code> if the teleport was successful
     */
    public boolean teleport(Entity destination, TeleportCause cause) {
        return player.teleport(destination, cause);
    }

    /**
     * Returns a list of entities within a bounding box centered around this
     * entity
     *
     * @param x 1/2 the size of the box along x axis
     * @param y 1/2 the size of the box along y axis
     * @param z 1/2 the size of the box along z axis
     * @return {@code List<Entity>} List of entities nearby
     */
    public List<Entity> getNearbyEntities(double x, double y, double z) {
        return player.getNearbyEntities(x, y, z);
    }

    /**
     * Returns a unique id for this entity
     *
     * @return Entity id
     */
    public int getEntityId() {
        return player.getEntityId();
    }

    /**
     * Returns the entity's current fire ticks (ticks before the entity stops
     * being on fire).
     *
     * @return int fireTicks
     */
    public int getFireTicks() {
        return player.getFireTicks();
    }

    /**
     * Returns the entity's maximum fire ticks.
     *
     * @return int maxFireTicks
     */
    public int getMaxFireTicks() {
        return player.getMaxFireTicks();
    }

    /**
     * Sets the entity's current fire ticks (ticks before the entity stops
     * being on fire).
     *
     * @param ticks Current ticks remaining
     */
    public void setFireTicks(int ticks) {
        player.setFireTicks(ticks);
    }

    /**
     * Mark the entity's removal.
     */
    public void remove() {
        player.remove();
    }

    /**
     * Returns true if this entity has been marked for removal.
     *
     * @return True if it is dead.
     */
    public boolean isDead() {
        return player.isDead();
    }

    /**
     * Returns false if the entity has died or been despawned for some other
     * reason.
     *
     * @return True if valid.
     */
    public boolean isValid() {
        return player.isValid();
    }

    /**
     * Gets the {@link Server} that contains this Entity
     *
     * @return Server instance running this Entity
     */
    public Server getServer() {
        return player.getServer();
    }

    /**
     * Gets the primary passenger of a vehicle. For vehicles that could have
     * multiple passengers, this will only return the primary passenger.
     *
     * @return an entity
     */
    public Entity getPassenger() {
        return player.getPassenger();
    }

    /**
     * Set the passenger of a vehicle.
     *
     * @param passenger The new passenger.
     * @return false if it could not be done for whatever reason
     */
    public boolean setPassenger(Entity passenger) {
        return player.setPassenger(passenger);
    }

    /**
     * Check if a vehicle has passengers.
     *
     * @return True if the vehicle has no passengers.
     */
    public boolean isEmpty() {
        return player.isEmpty();
    }

    /**
     * Eject any passenger.
     *
     * @return True if there was a passenger.
     */
    public boolean eject() {
        return player.eject();
    }

    /**
     * Returns the distance this entity has fallen
     *
     * @return The distance.
     */
    public float getFallDistance() {
        return player.getFallDistance();
    }

    /**
     * Sets the fall distance for this entity
     *
     * @param distance The new distance.
     */
    public void setFallDistance(float distance) {
        player.setFallDistance(distance);
    }

    /**
     * Record the last {@link EntityDamageEvent} inflicted on this entity
     *
     * @param event a {@link EntityDamageEvent}
     */
    public void setLastDamageCause(EntityDamageEvent event) {
        player.setLastDamageCause(event);
    }

    /**
     * Retrieve the last {@link EntityDamageEvent} inflicted on this entity.
     * This event may have been cancelled.
     *
     * @return the last known {@link EntityDamageEvent} or null if hitherto
     *     unharmed
     */
    public EntityDamageEvent getLastDamageCause() {
        return player.getLastDamageCause();
    }

    /**
     * Returns a unique and persistent id for this entity
     *
     * @return unique id
     */
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    /**
     * Gets the amount of ticks this entity has lived for.
     * <p>
     * This is the equivalent to "age" in entities.
     *
     * @return Age of entity
     */
    public int getTicksLived() {
        return player.getTicksLived();
    }

    /**
     * Sets the amount of ticks this entity has lived for.
     * <p>
     * This is the equivalent to "age" in entities. May not be less than one
     * tick.
     *
     * @param value Age of entity
     */
    public void setTicksLived(int value) {
        player.setTicksLived(value);
    }

    /**
     * Performs the specified {@link EntityEffect} for this entity.
     * <p>
     * This will be viewable to all players near the entity.
     *
     * @param type Effect to play.
     */
    public void playEffect(EntityEffect type) {
        player.playEffect(type);
    }

    /**
     * Get the type of the entity.
     *
     * @return The entity type.
     */
    public EntityType getType() {
        return player.getType();
    }

    /**
     * Returns whether this entity is inside a vehicle.
     *
     * @return True if the entity is in a vehicle.
     */
    public boolean isInsideVehicle() {
        return player.isInsideVehicle();
    }

    /**
     * Leave the current vehicle. If the entity is currently in a vehicle (and
     * is removed from it), true will be returned, otherwise false will be
     * returned.
     *
     * @return True if the entity was in a vehicle.
     */
    public boolean leaveVehicle() {
        return player.leaveVehicle();
    }

    /**
     * Get the vehicle that this player is inside. If there is no vehicle,
     * null will be returned.
     *
     * @return The current vehicle.
     */
    public Entity getVehicle() {
        return player.getVehicle();
    }

    /**
     * Sets a custom name on a mob. This name will be used in death messages
     * and can be sent to the client as a nameplate over the mob.
     * <p>
     * Setting the name to null or an empty string will clear it.
     * <p>
     * This value has no effect on players, they will always use their real
     * name.
     *
     * @param name the name to set
     */
    public void setCustomName(String name) {
        player.setCustomName(name);
    }

    /**
     * Gets the custom name on a mob. If there is no name this method will
     * return null.
     * <p>
     * This value has no effect on players, they will always use their real
     * name.
     *
     * @return name of the mob or null
     */
    public String getCustomName() {
        return player.getCustomName();
    }

    /**
     * Sets whether or not to display the mob's custom name client side. The
     * name will be displayed above the mob similarly to a player.
     * <p>
     * This value has no effect on players, they will always display their
     * name.
     *
     * @param flag custom name or not
     */
    public void setCustomNameVisible(boolean flag) {
        player.setCustomNameVisible(flag);
    }

    /**
     * Gets whether or not the mob's custom name is displayed client side.
     * <p>
     * This value has no effect on players, they will always display their
     * name.
     *
     * @return if the custom name is displayed
     */
    public boolean isCustomNameVisible() {
        return player.isCustomNameVisible();
    }

    /**
     * Sets whether the entity has a team colored (default: white) glow.
     *
     * @param flag if the entity is glowing
     */
    public void setGlowing(boolean flag) {
        player.setGlowing(flag);
    }

    /**
     * Gets whether the entity is glowing or not.
     *
     * @return whether the entity is glowing
     */
    public boolean isGlowing() {
        return player.isGlowing();
    }

    /**
     * Sets whether the entity is invulnerable or not.
     * <p>
     * When an entity is invulnerable it can only be damaged by players in
     * creative mode.
     *
     * @param flag if the entity is invulnerable
     */
    public void setInvulnerable(boolean flag) {
        player.setInvulnerable(flag);
    }

    /**
     * Gets whether the entity is invulnerable or not.
     *
     * @return whether the entity is
     */
    public boolean isInvulnerable() {
        return player.isInvulnerable();
    }

    /**
     * Gets whether the entity is silent or not.
     *
     * @return whether the entity is silent.
     */
    public boolean isSilent() {
        return player.isSilent();
    }

    /**
     * Sets whether the entity is silent or not.
     * <p>
     * When an entity is silent it will not produce any sound.
     *
     * @param flag if the entity is silent
     */
    public void setSilent(boolean flag) {
        player.setSilent(flag);
    }

    /**
     * Returns whether gravity applies to this entity.
     *
     * @return whether gravity applies
     */
    public boolean hasGravity() {
        return player.hasGravity();
    }

    /**
     * Sets whether gravity applies to this entity.
     *
     * @param gravity whether gravity should apply
     */
    public void setGravity(boolean gravity) {
        player.setGravity(gravity);
    }

    /**
     * Sets a metadata value in the implementing object's metadata store.
     *
     * @param metadataKey A unique key to identify this metadata.
     * @param newMetadataValue The metadata value to apply.
     * @throws IllegalArgumentException If value is null, or the owning plugin
     *     is null
     */
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        player.setMetadata(metadataKey, newMetadataValue);
    }

    /**
     * Returns a list of previously set metadata values from the implementing
     * object's metadata store.
     *
     * @param metadataKey the unique metadata key being sought.
     * @return A list of values, one for each plugin that has set the
     *     requested value.
     */
    public List<MetadataValue> getMetadata(String metadataKey) {
        return player.getMetadata(metadataKey);
    }

    /**
     * Tests to see whether the implementing object contains the given
     * metadata value in its metadata store.
     *
     * @param metadataKey the unique metadata key being queried.
     * @return the existence of the metadataKey within subject.
     */
    public boolean hasMetadata(String metadataKey) {
        return player.hasMetadata(metadataKey);
    }

    /**
     * Removes the given metadata value from the implementing object's
     * metadata store.
     *
     * @param metadataKey the unique metadata key identifying the metadata to
     *     remove.
     * @param owningPlugin This plugin's metadata value will be removed. All
     *     other values will be left untouched.
     * @throws IllegalArgumentException If plugin is null
     */
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        player.removeMetadata(metadataKey, owningPlugin);
    }

    /**
     * Sends this sender a message
     *
     * @param message Message to be displayed
     */
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    /**
     * Sends this sender multiple messages
     *
     * @param messages An array of messages to be displayed
     */
    public void sendMessage(String[] messages) {
        player.sendMessage(messages);
    }

    /**
     * Checks if this object contains an override for the specified
     * permission, by fully qualified name
     *
     * @param name Name of the permission
     * @return true if the permission is set, otherwise false
     */
    public boolean isPermissionSet(String name) {
        return player.isPermissionSet(name);
    }

    /**
     * Checks if this object contains an override for the specified {@link
     * Permission}
     *
     * @param perm Permission to check
     * @return true if the permission is set, otherwise false
     */
    public boolean isPermissionSet(Permission perm) {
        return player.isPermissionSet(perm);
    }

    /**
     * Gets the value of the specified permission, if set.
     * <p>
     * If a permission override is not set on this object, the default value
     * of the permission will be returned.
     *
     * @param name Name of the permission
     * @return Value of the permission
     */
    public boolean hasPermission(String name) {
        return player.hasPermission(name);
    }

    /**
     * Gets the value of the specified permission, if set.
     * <p>
     * If a permission override is not set on this object, the default value
     * of the permission will be returned
     *
     * @param perm Permission to get
     * @return Value of the permission
     */
    public boolean hasPermission(Permission perm) {
        return player.hasPermission(perm);
    }

    /**
     * Adds a new {@link PermissionAttachment} with a single permission by
     * name and value
     *
     * @param plugin Plugin responsible for this attachment, may not be null
     *     or disabled
     * @param name Name of the permission to attach
     * @param value Value of the permission
     * @return The PermissionAttachment that was just created
     */
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return player.addAttachment(plugin, name, value);
    }

    /**
     * Adds a new empty {@link PermissionAttachment} to this object
     *
     * @param plugin Plugin responsible for this attachment, may not be null
     *     or disabled
     * @return The PermissionAttachment that was just created
     */
    public PermissionAttachment addAttachment(Plugin plugin) {
        return player.addAttachment(plugin);
    }

    /**
     * Temporarily adds a new {@link PermissionAttachment} with a single
     * permission by name and value
     *
     * @param plugin Plugin responsible for this attachment, may not be null
     *     or disabled
     * @param name Name of the permission to attach
     * @param value Value of the permission
     * @param ticks Amount of ticks to automatically remove this attachment
     *     after
     * @return The PermissionAttachment that was just created
     */
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return player.addAttachment(plugin, name, value, ticks);
    }

    /**
     * Temporarily adds a new empty {@link PermissionAttachment} to this
     * object
     *
     * @param plugin Plugin responsible for this attachment, may not be null
     *     or disabled
     * @param ticks Amount of ticks to automatically remove this attachment
     *     after
     * @return The PermissionAttachment that was just created
     */
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return player.addAttachment(plugin, ticks);
    }

    /**
     * Removes the given {@link PermissionAttachment} from this object
     *
     * @param attachment Attachment to remove
     * @throws IllegalArgumentException Thrown when the specified attachment
     *     isn't part of this object
     */
    public void removeAttachment(PermissionAttachment attachment) {
        player.removeAttachment(attachment);
    }

    /**
     * Recalculates the permissions for this object, if the attachments have
     * changed values.
     * <p>
     * This should very rarely need to be called from a plugin.
     */
    public void recalculatePermissions() {
        player.recalculatePermissions();
    }

    /**
     * Gets a set containing all of the permissions currently in effect by
     * this object
     *
     * @return Set of currently effective permissions
     */
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return player.getEffectivePermissions();
    }

    /**
     * Checks if this object is a server operator
     *
     * @return true if this is an operator, otherwise false
     */
    public boolean isOp() {
        return player.isOp();
    }

    /**
     * Sets the operator status of this object
     *
     * @param value New operator value
     */
    public void setOp(boolean value) {
        player.setOp(value);
    }

    /**
     * Deals the given amount of damage to this entity.
     *
     * @param amount Amount of damage to deal
     */
    public void damage(double amount) {
        player.damage(amount);
    }

    /**
     * This method exists for legacy reasons to provide backwards
     * compatibility. It will not exist at runtime and should not be used
     * under any circumstances.
     *
     * @param amount Amount of damage to deal
     */
    @Deprecated
    public void _INVALID_damage(int amount) {
        player._INVALID_damage(amount);
    }

    /**
     * Deals the given amount of damage to this entity, from a specified
     * entity.
     *  @param amount Amount of damage to deal
     * @param source Entity which to attribute this damage from
     */
    public void damage(double amount, Entity source) {
        player.damage(amount, source);
    }

    /**
     * This method exists for legacy reasons to provide backwards
     * compatibility. It will not exist at runtime and should not be used
     * under any circumstances.
     *  @param amount Amount of damage to deal
     * @param source Entity which to attribute this damage from
     */
    @Deprecated
    public void _INVALID_damage(int amount, Entity source) {
        player._INVALID_damage(amount, source);
    }

    /**
     * Gets the entity's health from 0 to {@link #getMaxHealth()}, where 0 is dead.
     *
     * @return Health represented from 0 to max
     */
    public double getHealth() {
        return player.getHealth();
    }

    /**
     * This method exists for legacy reasons to provide backwards
     * compatibility. It will not exist at runtime and should not be used
     * under any circumstances.
     *
     * @return Health represented from 0 to max
     */
    @Deprecated
    public int _INVALID_getHealth() {
        return player._INVALID_getHealth();
    }

    /**
     * Sets the entity's health from 0 to {@link #getMaxHealth()}, where 0 is
     * dead.
     *
     * @param health New health represented from 0 to max
     * @throws IllegalArgumentException Thrown if the health is {@literal < 0 or >}
     *     {@link #getMaxHealth()}
     */
    public void setHealth(double health) {
        player.setHealth(health);
    }

    /**
     * This method exists for legacy reasons to provide backwards
     * compatibility. It will not exist at runtime and should not be used
     * under any circumstances.
     *
     * @param health New health represented from 0 to max
     * @throws IllegalArgumentException Thrown if the health is {@literal < 0 or >}
     *     {@link #getMaxHealth()}
     */
    @Deprecated
    public void _INVALID_setHealth(int health) {
        player._INVALID_setHealth(health);
    }

    /**
     * Gets the maximum health this entity has.
     *
     * @return Maximum health
     */
    public double getMaxHealth() {
        return player.getMaxHealth();
    }

    /**
     * This method exists for legacy reasons to provide backwards
     * compatibility. It will not exist at runtime and should not be used
     * under any circumstances.
     *
     * @return Maximum health
     */
    @Deprecated
    public int _INVALID_getMaxHealth() {
        return player._INVALID_getMaxHealth();
    }

    /**
     * Sets the maximum health this entity can have.
     * <p>
     * If the health of the entity is above the value provided it will be set
     * to that value.
     * <p>
     * Note: An entity with a health bar ({@link Player}, {@link EnderDragon},
     * {@link Wither}, etc...} will have their bar scaled accordingly.
     *
     * @param health amount of health to set the maximum to
     */
    public void setMaxHealth(double health) {
        player.setMaxHealth(health);
    }

    /**
     * This method exists for legacy reasons to provide backwards
     * compatibility. It will not exist at runtime and should not be used
     * under any circumstances.
     *
     * @param health amount of health to set the maximum to
     */
    @Deprecated
    public void _INVALID_setMaxHealth(int health) {
        player._INVALID_setMaxHealth(health);
    }

    /**
     * Resets the max health to the original amount.
     */
    public void resetMaxHealth() {
        player.resetMaxHealth();
    }

    /**
     * Launches a {@link Projectile} from the ProjectileSource.
     *
     * @param projectile class of the projectile to launch
     * @return the launched projectile
     */
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return player.launchProjectile(projectile);
    }

    /**
     * Launches a {@link Projectile} from the ProjectileSource with an
     * initial velocity.
     *
     * @param projectile class of the projectile to launch
     * @param velocity the velocity with which to launch
     * @return the launched projectile
     */
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        return player.launchProjectile(projectile, velocity);
    }

    /**
     * Tests to see of a Conversable object is actively engaged in a
     * conversation.
     *
     * @return True if a conversation is in progress
     */
    public boolean isConversing() {
        return player.isConversing();
    }

    /**
     * Accepts input into the active conversation. If no conversation is in
     * progress, this method does nothing.
     *
     * @param input The input message into the conversation
     */
    public void acceptConversationInput(String input) {
        player.acceptConversationInput(input);
    }

    /**
     * Enters into a dialog with a Conversation object.
     *
     * @param conversation The conversation to begin
     * @return True if the conversation should proceed, false if it has been
     *     enlobbyd
     */
    public boolean beginConversation(Conversation conversation) {
        return player.beginConversation(conversation);
    }

    /**
     * Abandons an active conversation.
     *
     * @param conversation The conversation to abandon
     */
    public void abandonConversation(Conversation conversation) {
        player.abandonConversation(conversation);
    }

    /**
     * Abandons an active conversation.
     *  @param conversation The conversation to abandon
     * @param details Details about why the conversation was abandoned
     */
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        player.abandonConversation(conversation, details);
    }

    /**
     * Checks if this player is currently online
     *
     * @return true if they are online
     */
    public boolean isOnline() {
        return player.isOnline();
    }

    /**
     * Checks if this player is banned or not
     *
     * @return true if banned, otherwise false
     */
    public boolean isBanned() {
        return player.isBanned();
    }

    /**
     * Bans or unbans this player
     *
     * @param banned true if banned
     * @deprecated Use {@link BanList#addBan(String, String, Date,
     *     String)} or {@link BanList#pardon(String)} to enhance
     *     functionality
     */
    @Deprecated
    public void setBanned(boolean banned) {
        player.setBanned(banned);
    }

    /**
     * Checks if this player is whitelisted or not
     *
     * @return true if whitelisted
     */
    public boolean isWhitelisted() {
        return player.isWhitelisted();
    }

    /**
     * Sets if this player is whitelisted or not
     *
     * @param value true if whitelisted
     */
    public void setWhitelisted(boolean value) {
        player.setWhitelisted(value);
    }

    /**
     * Gets a {@link Player} object that this represents, if there is one
     * <p>
     * If the player is online, this will return that player. Otherwise,
     * it will return null.
     *
     * @return Online player
     */
    public Player getPlayer() {
        return player.getPlayer();
    }

    /**
     * Gets the first date and time that this player was witnessed on this
     * server.
     * <p>
     * If the player has never played before, this will return 0. Otherwise,
     * it will be the amount of milliseconds since midnight, January 1, 1970
     * UTC.
     *
     * @return Date of first log-in for this player, or 0
     */
    public long getFirstPlayed() {
        return player.getFirstPlayed();
    }

    /**
     * Gets the last date and time that this player was witnessed on this
     * server.
     * <p>
     * If the player has never played before, this will return 0. Otherwise,
     * it will be the amount of milliseconds since midnight, January 1, 1970
     * UTC.
     *
     * @return Date of last log-in for this player, or 0
     */
    public long getLastPlayed() {
        return player.getLastPlayed();
    }

    /**
     * Checks if this player has played on this server before.
     *
     * @return True if the player has played before, otherwise false
     */
    public boolean hasPlayedBefore() {
        return player.hasPlayedBefore();
    }

    /**
     * Creates a Map representation of this class.
     * <p>
     * This class must provide a method to restore this class, as defined in
     * the {@link ConfigurationSerializable} interface javadocs.
     *
     * @return Map containing the current state of this class
     */
    public Map<String, Object> serialize() {
        return player.serialize();
    }

    /**
     * Sends this recipient a Plugin Message on the specified outgoing
     * channel.
     * <p>
     * The message may not be larger than {@link Messenger#MAX_MESSAGE_SIZE}
     * bytes, and the plugin must be registered to send messages on the
     * specified channel.
     *
     * @param source The plugin that sent this message.
     * @param channel The channel to send this message on.
     * @param message The raw message to send.
     * @throws IllegalArgumentException Thrown if the source plugin is
     *     disabled.
     * @throws IllegalArgumentException Thrown if source, channel or message
     *     is null.
     * @throws MessageTooLargeException Thrown if the message is too big.
     * @throws ChannelNotRegisteredException Thrown if the channel is not
     *     registered for this plugin.
     */
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        player.sendPluginMessage(source, channel, message);
    }

    /**
     * Gets a set containing all the Plugin Channels that this client is
     * listening on.
     *
     * @return Set containing all the channels that this client may accept.
     */
    public Set<String> getListeningPluginChannels() {
        return player.getListeningPluginChannels();
    }
}
