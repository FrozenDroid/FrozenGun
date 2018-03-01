package com.frozendroid.frozengun.events

import com.frozendroid.frozengun.interfaces.Messageable
import com.frozendroid.frozengun.models.Lobby
import com.frozendroid.frozengun.models.MinigamePlayer

class PlayerAlreadyInMatchEvent(messageable: Messageable,
                                val player: MinigamePlayer): MessageEvent(messageable) {
}