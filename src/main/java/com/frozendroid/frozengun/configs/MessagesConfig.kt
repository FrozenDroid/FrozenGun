package com.frozendroid.frozengun.configs

import com.frozendroid.frozengun.utils.ConfigLoader
import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration

class MessagesConfig {

    companion object {
        lateinit var config: FileConfiguration
        @JvmStatic
        var prefix = "FrozenGun"

        @JvmStatic
        fun loadMessages() {
            config = ConfigLoader.getMessagesConfig()
            prefix = ChatColor.translateAlternateColorCodes('&', config.getString("prefix"))
        }
    }

}