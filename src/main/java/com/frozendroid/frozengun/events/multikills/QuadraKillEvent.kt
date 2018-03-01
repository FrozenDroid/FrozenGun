package com.frozendroid.frozengun.events.multikills

import com.frozendroid.frozengun.events.MessageEvent
import com.frozendroid.frozengun.interfaces.Messageable
import com.frozendroid.frozengun.models.MinigamePlayer

class QuadraKillEvent(messageable: Messageable,
                      val player: MinigamePlayer) : MessageEvent(messageable) {
}