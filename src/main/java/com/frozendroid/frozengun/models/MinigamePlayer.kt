package com.frozendroid.frozengun.models

import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.PLUGIN
import org.bukkit.inventory.ItemStack

class MinigamePlayer(p: Player) : Player by p {

    var weapons: ArrayList<Weapon> = ArrayList()
    var match: Match? = null
    var lobby: Lobby? = null

    var lastLocation: Location? = null
    var lastInventory: Array<ItemStack> = arrayOf()
    var lastGamemode: GameMode? = null
    var lastFoodLevel: Int? = null
    var lastHealth: Double? = null
    var lastMaxHealth: Double? = null
    var lastExp: Float? = null

    /**
     * Save the player's current state, so that it can later be restored
     */
    fun saveCurrentState() {
        this.lastLocation = this.location
        this.lastHealth = this.health
        this.lastMaxHealth = this.maxHealth
        this.lastInventory = this.inventory.contents
        this.lastExp = this.exp
        this.lastFoodLevel = this.foodLevel
        this.lastGamemode = this.gameMode
    }

    /**
     * Restore the player's state to what it was when it was saved.
     * If it was not saved, then there are some fallback defaults. This should never be the case, though.
     */
    fun restoreState() {
        // Teleport to last location, otherwise teleport to bed spawn.
        this.teleport(this.lastLocation ?: this.bedSpawnLocation)

        this.health = this.lastHealth ?: 20.0
        this.maxHealth = this.lastMaxHealth ?: 20.0
        this.gameMode = this.gameMode ?: GameMode.SURVIVAL
        this.exp = this.lastExp ?: 0.0F
        this.foodLevel = this.lastFoodLevel ?: 0
        this.inventory.contents = this.lastInventory
    }

    /**
     * Get the weapon in the main hand
     */
    fun getWeaponInHand(): Weapon? {
        val itemStack = player.inventory.itemInMainHand

        return weapons.firstOrNull {
            it.material == itemStack.type && it.name == itemStack.itemMeta.displayName
        }
    }

    /**
     * Give the player a weapon, adding it to their inventory.
     *
     * @param weapon The weapon to add.
     */
    private fun addWeapon(weapon: Weapon) {
        weapons.add(weapon)
        val itemStack = ItemStack(weapon.material, 1)
        val meta = itemStack.itemMeta
        meta.displayName = weapon.name
        itemStack.itemMeta = meta

        inventory.addItem(itemStack)
    }

    /**
     * Respawn the player.
     * If the player is respawned when they are not in a match, their original state will be restored instead.
     */
    fun respawn() {
        if (match == null) {
            restoreState()
            return
        }
        health = getAttribute(Attribute.GENERIC_MAX_HEALTH).value
        teleport(match!!.feasibleSpawn.location, PLUGIN)
    }

    /**
     * Make the player join a match. Will set up their state for the arena, and start the cooldown bar scheduler.
     * Don't confuse this function for joining the queue.
     *
     * @param match The match to join.
     */
    fun join(match: Match) {
        this.match = match
        health = 20.0
        foodLevel = 20
        walkSpeed = this.match?.arena?.runSpeed ?: 1.0F
        inventory.heldItemSlot = 0
        gameMode = GameMode.SURVIVAL
        inventory.clear()
        match.arena.weapons.forEach {
            val weapon = it.clone()
            weapon.player = this
            addWeapon(weapon)
        }
        teleport(match.feasibleSpawn.location, PLUGIN)
        match.startCooldownBar(this)
        match.addPlayer(this)
    }

    fun inLobby(): Boolean {
        return this.lobby != null
    }

    fun isInMatch(): Boolean {
        return this.match != null
    }

}