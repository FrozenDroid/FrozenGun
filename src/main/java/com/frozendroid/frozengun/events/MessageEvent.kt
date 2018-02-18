package com.frozendroid.frozengun.events

import com.frozendroid.frozengun.configs.MessagesConfig
import com.frozendroid.frozengun.interfaces.Messageable
import com.frozendroid.frozengun.listeners.MessageListener
import com.google.common.base.CaseFormat
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import java.lang.reflect.Method

abstract class MessageEvent(vararg val messageable: Messageable) : Event() {

    @Suppress("MemberVisibilityCanBePrivate")
    var prefix = "No prefix"
    val silent = false

    init {
        checkLookupCaches(this.javaClass)
        prefix = MessagesConfig.prefix
    }


    companion object {
        @JvmStatic
        val handlerList: HandlerList = HandlerList()

        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        fun checkLookupCaches(clazz: Class<out MessageEvent>) {
            if (MessageListener.lookupCache.containsKey(clazz)) {
                return
            }

            val replaceMap = hashMapOf<String, ArrayList<Method>>()

            val name = CaseFormat.UPPER_CAMEL.to(
                    CaseFormat.LOWER_UNDERSCORE,
                    clazz.simpleName.replace("Event", "")
            )

            var message: String? = MessagesConfig.config.getString(name)

            if (message == null || message.isEmpty()) {
                Bukkit.getLogger().warning("No message found for $name")
                return
            }

            val regex = Regex("\\{\\{(\\S*)}}")
            val result = regex.findAll(message)
            for (matchResult in result) {
                var patternIndex = 0
                val toReplace = matchResult.value
                val pattern = matchResult.groups[1]?.value?.trim()?.split(".")?: continue
                var current: Class<Any>? = clazz as Class<Any>

                val methodList = arrayListOf<Method>()

                while (current != null) {
                    val methodMatch = current.methods
                            .filter { it.name.startsWith("get") }
                            .find {
                                val methodGetterName = CaseFormat.UPPER_CAMEL.to(
                                        CaseFormat.LOWER_UNDERSCORE,
                                        it.name.replace("get", "")
                                )
                                methodGetterName == pattern[patternIndex]
                            }
                    if (methodMatch == null) {
                        println("Error while trying to fill in $message, couldn't find ${pattern[patternIndex]}")
                        return
                    }
                    methodMatch.isAccessible = true

                    current = methodMatch.returnType as Class<Any>?
                    methodList.add(methodMatch)
                    if (patternIndex >= pattern.size - 1) break

                    patternIndex++
                }

                replaceMap[toReplace] = methodList

                message = message?.replace(toReplace, current.toString())
            }
            MessageListener.lookupCache[clazz] = replaceMap
        }
    }

    override fun getHandlers(): HandlerList {
        return MessageEvent.handlerList
    }

}
