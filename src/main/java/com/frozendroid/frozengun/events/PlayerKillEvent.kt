package com.frozendroid.frozengun.events

import com.frozendroid.frozengun.interfaces.Messageable
import com.frozendroid.frozengun.models.Lobby
import com.frozendroid.frozengun.models.MinigamePlayer

class PlayerKillEvent(messageable: Messageable,
                      val shooter: MinigamePlayer,
                      val victim: MinigamePlayer) : MessageEvent(messageable) {
}