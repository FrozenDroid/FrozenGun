package com.frozendroid.frozengun.events

import com.frozendroid.frozengun.interfaces.Messageable
import com.frozendroid.frozengun.models.MinigamePlayer

class CantFindArenaEvent(messageable: Messageable, player: MinigamePlayer) : MessageEvent(messageable) {
}