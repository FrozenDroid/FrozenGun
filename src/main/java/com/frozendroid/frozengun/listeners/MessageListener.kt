package com.frozendroid.frozengun.listeners

import com.frozendroid.frozengun.configs.MessagesConfig
import com.frozendroid.frozengun.events.MessageEvent
import com.google.common.base.CaseFormat
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import java.lang.reflect.Method

class MessageListener(plugin: Plugin) : Listener {

    companion object {
        val lookupCache = hashMapOf<Class<out MessageEvent>?, HashMap<String, ArrayList<Method>>>()
    }

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler()
    fun onMessageEvent(event: MessageEvent) {
        if (event.silent) return

        val name = CaseFormat.UPPER_CAMEL.to(
                CaseFormat.LOWER_UNDERSCORE,
                event.eventName.replace("Event", "")
        )

        var message: String? = MessagesConfig.config.getString(name)

        lookupCache[event.javaClass]?.forEach {
            var current: Any = event
            it.value.forEach {
                current = it.invoke(current)
            }
            message = message?.replace(it.key, current.toString())
        }

        event.messageable.forEach { it.sendMessage(message) }
    }

}