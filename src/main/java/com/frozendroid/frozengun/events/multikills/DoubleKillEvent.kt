package com.frozendroid.frozengun.events.multikills

import com.frozendroid.frozengun.events.MessageEvent
import com.frozendroid.frozengun.interfaces.Messageable
import com.frozendroid.frozengun.models.MinigamePlayer

class DoubleKillEvent(messageable: Messageable,
                      val player: MinigamePlayer) : MessageEvent(messageable) {
}