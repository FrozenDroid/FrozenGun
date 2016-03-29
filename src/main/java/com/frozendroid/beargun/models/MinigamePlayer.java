package com.frozendroid.beargun.models;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
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
import org.bukkit.util.Vector;

import java.net.InetSocketAddress;
import java.util.*;

public class MinigamePlayer {
    private ArrayList<Weapon> weapons = new ArrayList<>();

    private Player player = null;
    private Match match;
    private Location lastLocation;
    private ItemStack[] lastInventoryContents;
    private GameMode lastGamemode;
    private int lastFoodLevel;
    private double lastHealth;
    private double lastMaxHealth;
    private float lastExp;

    public MinigamePlayer(Player player)
    {
        this.player = player;
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
        ItemStack itemStack = getItemInHand();
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
        Player player = getPlayer();
        player.setHealth(20);
        player.teleport(match.getFeasibleSpawn().getLocation());
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

    public String getDisplayName()
    {
        return player.getDisplayName();
    }

    public String getCustomName()
    {
        return player.getCustomName();
    }

    public Scoreboard getScoreboard()
    {
        return player.getScoreboard();
    }

    public void saveData()
    {
        player.saveData();
    }

    public Block getTargetBlock(Set<Material> set, int i)
    {
        return player.getTargetBlock(set, i);
    }

    public void resetPlayerTime()
    {
        player.resetPlayerTime();
    }

    public Inventory getEnderChest()
    {
        return player.getEnderChest();
    }

    /** @deprecated
     * @param b */
    @Deprecated
    public void setBanned(boolean b)
    {
        player.setBanned(b);
    }

    public boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause teleportCause)
    {
        return player.teleport(entity, teleportCause);
    }

    public boolean setPassenger(Entity entity)
    {
        return player.setPassenger(entity);
    }

    public Location getBedSpawnLocation()
    {
        return player.getBedSpawnLocation();
    }

    public boolean isEmpty()
    {
        return player.isEmpty();
    }

    public boolean isWhitelisted()
    {
        return player.isWhitelisted();
    }

    public void setStatistic(Statistic statistic, int i) throws IllegalArgumentException
    {
        player.setStatistic(statistic, i);
    }

    /** @deprecated
     * @param s
     * @param s1 */
    @Deprecated
    public void sendTitle(String s, String s1)
    {
        player.sendTitle(s, s1);
    }

    /** @deprecated
     * @param location
     * @param material
     * @param b */
    @Deprecated
    public void sendBlockChange(Location location, Material material, byte b)
    {
        player.sendBlockChange(location, material, b);
    }

    public void setVelocity(Vector vector)
    {
        player.setVelocity(vector);
    }

    public void abandonConversation(Conversation conversation)
    {
        player.abandonConversation(conversation);
    }

    public void setPlayerTime(long l, boolean b)
    {
        player.setPlayerTime(l, b);
    }

    public boolean addPotionEffect(PotionEffect potionEffect)
    {
        return player.addPotionEffect(potionEffect);
    }

    public double getEyeHeight(boolean b)
    {
        return player.getEyeHeight(b);
    }

    public void setAllowFlight(boolean b)
    {
        player.setAllowFlight(b);
    }

    public Entity getPassenger()
    {
        return player.getPassenger();
    }

    public GameMode getGameMode()
    {
        return player.getGameMode();
    }

    public World getWorld()
    {
        return player.getWorld();
    }

    public void setSprinting(boolean b)
    {
        player.setSprinting(b);
    }

    public void kickPlayer(String s)
    {
        player.kickPlayer(s);
    }

    public int getEntityId()
    {
        return player.getEntityId();
    }

    public boolean isConversing()
    {
        return player.isConversing();
    }

    public void setOp(boolean b)
    {
        player.setOp(b);
    }

    public void removePotionEffect(PotionEffectType potionEffectType)
    {
        player.removePotionEffect(potionEffectType);
    }

    public Collection<PotionEffect> getActivePotionEffects()
    {
        return player.getActivePotionEffects();
    }

    public boolean isHealthScaled()
    {
        return player.isHealthScaled();
    }

    public boolean isDead()
    {
        return player.isDead();
    }

    public void setFoodLevel(int i)
    {
        player.setFoodLevel(i);
    }

    public void setExp(float v)
    {
        player.setExp(v);
    }

    public Set<PermissionAttachmentInfo> getEffectivePermissions()
    {
        return player.getEffectivePermissions();
    }

    public boolean canSee(Player player)
    {
        return this.player.canSee(player);
    }

    public Player getKiller()
    {
        return player.getKiller();
    }

    public void setPlayerListName(String s)
    {
        player.setPlayerListName(s);
    }

    public PermissionAttachment addAttachment(Plugin plugin)
    {
        return player.addAttachment(plugin);
    }

    public Location getLocation(Location location)
    {
        return player.getLocation(location);
    }

    public boolean hasPermission(String s)
    {
        return player.hasPermission(s);
    }

    public void setSaturation(float v)
    {
        player.setSaturation(v);
    }

    public void setMaximumAir(int i)
    {
        player.setMaximumAir(i);
    }

    public int getLevel()
    {
        return player.getLevel();
    }

    public void showPlayer(Player player)
    {
        this.player.showPlayer(player);
    }

    public boolean teleport(Entity entity)
    {
        return player.teleport(entity);
    }

    public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException
    {
        player.incrementStatistic(statistic, entityType);
    }

    public EntityType getType()
    {
        return player.getType();
    }

    public String getPlayerListName()
    {
        return player.getPlayerListName();
    }

    public void setMaximumNoDamageTicks(int i)
    {
        player.setMaximumNoDamageTicks(i);
    }

    public void removeMetadata(String s, Plugin plugin)
    {
        player.removeMetadata(s, plugin);
    }

    public void setBedSpawnLocation(Location location)
    {
        player.setBedSpawnLocation(location);
    }

    /** @deprecated
     * @param s */
    @Deprecated
    public void setTexturePack(String s)
    {
        player.setTexturePack(s);
    }

    public Player getPlayer()
    {
        return player.getPlayer();
    }

    public boolean isOnline()
    {
        return player.isOnline();
    }

    public double getEyeHeight()
    {
        return player.getEyeHeight();
    }

    public void setCanPickupItems(boolean b)
    {
        player.setCanPickupItems(b);
    }

    public boolean getCanPickupItems()
    {
        return player.getCanPickupItems();
    }

    public void setHealthScaled(boolean b)
    {
        player.setHealthScaled(b);
    }

    public <T extends Projectile> T launchProjectile(Class<? extends T> aClass, Vector vector)
    {
        return player.launchProjectile(aClass, vector);
    }

    public Server getServer()
    {
        return player.getServer();
    }

    public void setRemainingAir(int i)
    {
        player.setRemainingAir(i);
    }

    public boolean performCommand(String s)
    {
        return player.performCommand(s);
    }

    public long getFirstPlayed()
    {
        return player.getFirstPlayed();
    }

    public void setCustomName(String s)
    {
        player.setCustomName(s);
    }

    public boolean setWindowProperty(InventoryView.Property property, int i)
    {
        return player.setWindowProperty(property, i);
    }

    /** @deprecated
     * @param location
     * @param i
     * @param i1
     * @param i2
     * @param bytes */
    @Deprecated
    public boolean sendChunkChange(Location location, int i, int i1, int i2, byte[] bytes)
    {
        return player.sendChunkChange(location, i, i1, i2, bytes);
    }

    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause teleportCause)
    {
        return player.teleport(location, teleportCause);
    }

    public void setStatistic(Statistic statistic, EntityType entityType, int i)
    {
        player.setStatistic(statistic, entityType, i);
    }

    public InetSocketAddress getAddress()
    {
        return player.getAddress();
    }

    public double getHealth()
    {
        return player.getHealth();
    }

    public boolean isSneaking()
    {
        return player.isSneaking();
    }

    public void sendPluginMessage(Plugin plugin, String s, byte[] bytes)
    {
        player.sendPluginMessage(plugin, s, bytes);
    }

    public int getTicksLived()
    {
        return player.getTicksLived();
    }

    public void setTicksLived(int i)
    {
        player.setTicksLived(i);
    }

    public void closeInventory()
    {
        player.closeInventory();
    }

    public ItemStack getItemOnCursor()
    {
        return player.getItemOnCursor();
    }

    public void damage(double v)
    {
        player.damage(v);
    }

    public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException
    {
        player.incrementStatistic(statistic, material);
    }

    public void decrementStatistic(Statistic statistic, EntityType entityType, int i)
    {
        player.decrementStatistic(statistic, entityType, i);
    }

    public void setSpectatorTarget(Entity entity)
    {
        player.setSpectatorTarget(entity);
    }

    public void setSleepingIgnored(boolean b)
    {
        player.setSleepingIgnored(b);
    }

    public int getStatistic(Statistic statistic) throws IllegalArgumentException
    {
        return player.getStatistic(statistic);
    }

    public float getWalkSpeed()
    {
        return player.getWalkSpeed();
    }

    public void setFireTicks(int i)
    {
        player.setFireTicks(i);
    }

    public void setMaxHealth(double v)
    {
        player.setMaxHealth(v);
    }

    public void setLastDamageCause(EntityDamageEvent entityDamageEvent)
    {
        player.setLastDamageCause(entityDamageEvent);
    }

    public void setCompassTarget(Location location)
    {
        player.setCompassTarget(location);
    }

    public InventoryView openInventory(Inventory inventory)
    {
        return player.openInventory(inventory);
    }

    public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException
    {
        player.setScoreboard(scoreboard);
    }

    public WeatherType getPlayerWeather()
    {
        return player.getPlayerWeather();
    }

    public boolean isValid()
    {
        return player.isValid();
    }

    /** @deprecated
     * @param i */
    @Deprecated
    public void _INVALID_setHealth(int i)
    {
        player._INVALID_setHealth(i);
    }

    public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException
    {
        return player.getStatistic(statistic, entityType);
    }

    public void setWalkSpeed(float v) throws IllegalArgumentException
    {
        player.setWalkSpeed(v);
    }

    /** @deprecated
     * @param hashSet
     * @param i */
    @Deprecated
    public List<Block> getLastTwoTargetBlocks(HashSet<Byte> hashSet, int i)
    {
        return player.getLastTwoTargetBlocks(hashSet, i);
    }

    public void setLastDamage(double v)
    {
        player.setLastDamage(v);
    }

    public long getPlayerTime()
    {
        return player.getPlayerTime();
    }

    public double getHealthScale()
    {
        return player.getHealthScale();
    }

    public void playNote(Location location, Instrument instrument, Note note)
    {
        player.playNote(location, instrument, note);
    }

    public boolean hasPotionEffect(PotionEffectType potionEffectType)
    {
        return player.hasPotionEffect(potionEffectType);
    }

    public InventoryView openEnchanting(Location location, boolean b)
    {
        return player.openEnchanting(location, b);
    }

    public EntityEquipment getEquipment()
    {
        return player.getEquipment();
    }

    public void setHealth(double v)
    {
        player.setHealth(v);
    }

    public void incrementStatistic(Statistic statistic, int i) throws IllegalArgumentException
    {
        player.incrementStatistic(statistic, i);
    }

    public void setStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException
    {
        player.setStatistic(statistic, material, i);
    }

    public List<MetadataValue> getMetadata(String s)
    {
        return player.getMetadata(s);
    }

    public void decrementStatistic(Statistic statistic) throws IllegalArgumentException
    {
        player.decrementStatistic(statistic);
    }

    /** @deprecated */
    @Deprecated
    public void resetTitle()
    {
        player.resetTitle();
    }

    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b)
    {
        return player.addAttachment(plugin, s, b);
    }

    /** @deprecated
     * @param i */
    @Deprecated
    public void _INVALID_setLastDamage(int i)
    {
        player._INVALID_setLastDamage(i);
    }

    public boolean isInsideVehicle()
    {
        return player.isInsideVehicle();
    }

    public boolean leaveVehicle()
    {
        return player.leaveVehicle();
    }

    public void sendRawMessage(String s)
    {
        player.sendRawMessage(s);
    }

    public void setTotalExperience(int i)
    {
        player.setTotalExperience(i);
    }

    public float getExhaustion()
    {
        return player.getExhaustion();
    }

    public Map<String, Object> serialize()
    {
        return player.serialize();
    }

    public boolean isSleeping()
    {
        return player.isSleeping();
    }

    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i)
    {
        return player.addAttachment(plugin, s, b, i);
    }

    public boolean setLeashHolder(Entity entity)
    {
        return player.setLeashHolder(entity);
    }

    public void decrementStatistic(Statistic statistic, int i) throws IllegalArgumentException
    {
        player.decrementStatistic(statistic, i);
    }

    public void setSneaking(boolean b)
    {
        player.setSneaking(b);
    }

    public boolean hasAchievement(Achievement achievement)
    {
        return player.hasAchievement(achievement);
    }

    /** @deprecated */
    @Deprecated
    public int _INVALID_getHealth()
    {
        return player._INVALID_getHealth();
    }

    public int getMaxFireTicks()
    {
        return player.getMaxFireTicks();
    }

    public long getLastPlayed()
    {
        return player.getLastPlayed();
    }

    public boolean isPermissionSet(String s)
    {
        return player.isPermissionSet(s);
    }

    public Set<String> getListeningPluginChannels()
    {
        return player.getListeningPluginChannels();
    }

    public boolean getAllowFlight()
    {
        return player.getAllowFlight();
    }

    public void removeAchievement(Achievement achievement)
    {
        player.removeAchievement(achievement);
    }

    public Entity getLeashHolder() throws IllegalStateException
    {
        return player.getLeashHolder();
    }

    public boolean isBanned()
    {
        return player.isBanned();
    }

    /** @deprecated
     * @param location
     * @param effect
     * @param i */
    @Deprecated
    public void playEffect(Location location, Effect effect, int i)
    {
        player.playEffect(location, effect, i);
    }

    /** @deprecated
     * @param i
     * @param entity */
    @Deprecated
    public void _INVALID_damage(int i, Entity entity)
    {
        player._INVALID_damage(i, entity);
    }

    public boolean isSleepingIgnored()
    {
        return player.isSleepingIgnored();
    }

    public float getFlySpeed()
    {
        return player.getFlySpeed();
    }

    /** @deprecated
     * @param i */
    @Deprecated
    public void _INVALID_setMaxHealth(int i)
    {
        player._INVALID_setMaxHealth(i);
    }

    public boolean isOp()
    {
        return player.isOp();
    }

    public boolean teleport(Location location)
    {
        return player.teleport(location);
    }

    public boolean getRemoveWhenFarAway()
    {
        return player.getRemoveWhenFarAway();
    }

    public void resetPlayerWeather()
    {
        player.resetPlayerWeather();
    }

    public void setFlySpeed(float v) throws IllegalArgumentException
    {
        player.setFlySpeed(v);
    }

    public boolean isLeashed()
    {
        return player.isLeashed();
    }

    public void sendMap(MapView mapView)
    {
        player.sendMap(mapView);
    }

    public void updateInventory()
    {
        player.updateInventory();
    }

    public boolean addPotionEffects(Collection<PotionEffect> collection)
    {
        return player.addPotionEffects(collection);
    }

    public void playSound(Location location, String s, float v, float v1)
    {
        player.playSound(location, s, v, v1);
    }

    public double getMaxHealth()
    {
        return player.getMaxHealth();
    }

    public void sendSignChange(Location location, String[] strings) throws IllegalArgumentException
    {
        player.sendSignChange(location, strings);
    }

    public Location getCompassTarget()
    {
        return player.getCompassTarget();
    }

    public boolean hasMetadata(String s)
    {
        return player.hasMetadata(s);
    }

    public void removeAttachment(PermissionAttachment permissionAttachment)
    {
        player.removeAttachment(permissionAttachment);
    }

    public InventoryView getOpenInventory()
    {
        return player.getOpenInventory();
    }

    public void sendMessage(String[] strings)
    {
        player.sendMessage(strings);
    }

    /** @deprecated
     * @param location
     * @param b
     * @param b1 */
    @Deprecated
    public void playNote(Location location, byte b, byte b1)
    {
        player.playNote(location, b, b1);
    }

    public void setLevel(int i)
    {
        player.setLevel(i);
    }

    public void setItemInHand(ItemStack itemStack)
    {
        player.setItemInHand(itemStack);
    }

    public UUID getUniqueId()
    {
        return player.getUniqueId();
    }

    public boolean hasLineOfSight(Entity entity)
    {
        return player.hasLineOfSight(entity);
    }

    /** @deprecated */
    @Deprecated
    public boolean isOnGround()
    {
        return player.isOnGround();
    }

    public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException
    {
        player.decrementStatistic(statistic, material);
    }

    public void sendMessage(String s)
    {
        player.sendMessage(s);
    }

    public boolean hasPermission(Permission permission)
    {
        return player.hasPermission(permission);
    }

    public boolean beginConversation(Conversation conversation)
    {
        return player.beginConversation(conversation);
    }

    /** @deprecated
     * @param hashSet
     * @param i */
    @Deprecated
    public Block getTargetBlock(HashSet<Byte> hashSet, int i)
    {
        return player.getTargetBlock(hashSet, i);
    }

    public PermissionAttachment addAttachment(Plugin plugin, int i)
    {
        return player.addAttachment(plugin, i);
    }

    public void setHealthScale(double v) throws IllegalArgumentException
    {
        player.setHealthScale(v);
    }

    public boolean isCustomNameVisible()
    {
        return player.isCustomNameVisible();
    }

    public void setWhitelisted(boolean b)
    {
        player.setWhitelisted(b);
    }

    public void hidePlayer(Player player)
    {
        this.player.hidePlayer(player);
    }

    public boolean isFlying()
    {
        return player.isFlying();
    }

    public int getRemainingAir()
    {
        return player.getRemainingAir();
    }

    public void setRemoveWhenFarAway(boolean b)
    {
        player.setRemoveWhenFarAway(b);
    }

    public boolean eject()
    {
        return player.eject();
    }

    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent conversationAbandonedEvent)
    {
        player.abandonConversation(conversation, conversationAbandonedEvent);
    }

    public int getMaximumNoDamageTicks()
    {
        return player.getMaximumNoDamageTicks();
    }

    public String getName()
    {
        return player.getName();
    }

    public void setFlying(boolean b)
    {
        player.setFlying(b);
    }

    /** @deprecated
     * @param i */
    @Deprecated
    public void _INVALID_damage(int i)
    {
        player._INVALID_damage(i);
    }

    public Entity getSpectatorTarget()
    {
        return player.getSpectatorTarget();
    }

    public EntityDamageEvent getLastDamageCause()
    {
        return player.getLastDamageCause();
    }

    public <T> void playEffect(Location location, Effect effect, T t)
    {
        player.playEffect(location, effect, t);
    }

    public void setFallDistance(float v)
    {
        player.setFallDistance(v);
    }

    public void loadData()
    {
        player.loadData();
    }

    public int getSleepTicks()
    {
        return player.getSleepTicks();
    }

    /** @deprecated */
    @Deprecated
    public int _INVALID_getMaxHealth()
    {
        return player._INVALID_getMaxHealth();
    }

    public boolean isBlocking()
    {
        return player.isBlocking();
    }

    public void setItemOnCursor(ItemStack itemStack)
    {
        player.setItemOnCursor(itemStack);
    }

    public void setPlayerWeather(WeatherType weatherType)
    {
        player.setPlayerWeather(weatherType);
    }

    public void setBedSpawnLocation(Location location, boolean b)
    {
        player.setBedSpawnLocation(location, b);
    }

    /** @deprecated
     * @param location
     * @param i
     * @param b */
    @Deprecated
    public void sendBlockChange(Location location, int i, byte b)
    {
        player.sendBlockChange(location, i, b);
    }

    public int getFireTicks()
    {
        return player.getFireTicks();
    }

    public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException
    {
        return player.getStatistic(statistic, material);
    }

    public List<Block> getLineOfSight(Set<Material> set, int i)
    {
        return player.getLineOfSight(set, i);
    }

    public void playEffect(EntityEffect entityEffect)
    {
        player.playEffect(entityEffect);
    }

    /** @deprecated */
    @Deprecated
    public int _INVALID_getLastDamage()
    {
        return player._INVALID_getLastDamage();
    }

    public void giveExpLevels(int i)
    {
        player.giveExpLevels(i);
    }

    public void incrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException
    {
        player.incrementStatistic(statistic, material, i);
    }

    public void awardAchievement(Achievement achievement)
    {
        player.awardAchievement(achievement);
    }

    public void incrementStatistic(Statistic statistic) throws IllegalArgumentException
    {
        player.incrementStatistic(statistic);
    }

    public void resetMaxHealth()
    {
        player.resetMaxHealth();
    }

    public int getFoodLevel()
    {
        return player.getFoodLevel();
    }

    public PlayerInventory getInventory()
    {
        return player.getInventory();
    }

    public void acceptConversationInput(String s)
    {
        player.acceptConversationInput(s);
    }

    public void setExhaustion(float v)
    {
        player.setExhaustion(v);
    }

    public void setDisplayName(String s)
    {
        player.setDisplayName(s);
    }

    public boolean addPotionEffect(PotionEffect potionEffect, boolean b)
    {
        return player.addPotionEffect(potionEffect, b);
    }

    public int getNoDamageTicks()
    {
        return player.getNoDamageTicks();
    }

    public void setResourcePack(String s)
    {
        player.setResourcePack(s);
    }

    public <T extends Projectile> T launchProjectile(Class<? extends T> aClass)
    {
        return player.launchProjectile(aClass);
    }

    public void setMetadata(String s, MetadataValue metadataValue)
    {
        player.setMetadata(s, metadataValue);
    }

    public List<Entity> getNearbyEntities(double v, double v1, double v2)
    {
        return player.getNearbyEntities(v, v1, v2);
    }

    public float getSaturation()
    {
        return player.getSaturation();
    }

    public float getFallDistance()
    {
        return player.getFallDistance();
    }

    public double getLastDamage()
    {
        return player.getLastDamage();
    }

    public void openInventory(InventoryView inventoryView)
    {
        player.openInventory(inventoryView);
    }

    public void setNoDamageTicks(int i)
    {
        player.setNoDamageTicks(i);
    }

    public void setGameMode(GameMode gameMode)
    {
        player.setGameMode(gameMode);
    }

    public ItemStack getItemInHand()
    {
        return player.getItemInHand();
    }

    public int getTotalExperience()
    {
        return player.getTotalExperience();
    }

    public int getExpToLevel()
    {
        return player.getExpToLevel();
    }

    public InventoryView openWorkbench(Location location, boolean b)
    {
        return player.openWorkbench(location, b);
    }

    public void playSound(Location location, Sound sound, float v, float v1)
    {
        player.playSound(location, sound, v, v1);
    }

    public float getExp()
    {
        return player.getExp();
    }

    public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException
    {
        player.decrementStatistic(statistic, entityType);
    }

    public boolean isPermissionSet(Permission permission)
    {
        return player.isPermissionSet(permission);
    }

    public void damage(double v, Entity entity)
    {
        player.damage(v, entity);
    }

    public boolean isPlayerTimeRelative()
    {
        return player.isPlayerTimeRelative();
    }

    public boolean hasPlayedBefore()
    {
        return player.hasPlayedBefore();
    }

    public void remove()
    {
        player.remove();
    }

    public boolean isSprinting()
    {
        return player.isSprinting();
    }

    public void chat(String s)
    {
        player.chat(s);
    }

    public Vector getVelocity()
    {
        return player.getVelocity();
    }

    public int getMaximumAir()
    {
        return player.getMaximumAir();
    }

    public Location getLocation()
    {
        return player.getLocation();
    }

    public void incrementStatistic(Statistic statistic, EntityType entityType, int i) throws IllegalArgumentException
    {
        player.incrementStatistic(statistic, entityType, i);
    }

    public void setCustomNameVisible(boolean b)
    {
        player.setCustomNameVisible(b);
    }

    public void giveExp(int i)
    {
        player.giveExp(i);
    }

    public List<Block> getLastTwoTargetBlocks(Set<Material> set, int i)
    {
        return player.getLastTwoTargetBlocks(set, i);
    }

    public Entity getVehicle()
    {
        return player.getVehicle();
    }

    public Location getEyeLocation()
    {
        return player.getEyeLocation();
    }

    public Player.Spigot spigot()
    {
        return player.spigot();
    }

    /** @deprecated
     * @param hashSet
     * @param i */
    @Deprecated
    public List<Block> getLineOfSight(HashSet<Byte> hashSet, int i)
    {
        return player.getLineOfSight(hashSet, i);
    }

    public void recalculatePermissions()
    {
        player.recalculatePermissions();
    }

    public void decrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException
    {
        player.decrementStatistic(statistic, material, i);
    }

    public long getPlayerTimeOffset()
    {
        return player.getPlayerTimeOffset();
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
}
