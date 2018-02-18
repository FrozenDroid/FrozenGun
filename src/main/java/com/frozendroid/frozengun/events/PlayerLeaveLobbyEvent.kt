package com.frozendroid.frozengun.events

import com.frozendroid.frozengun.interfaces.Messageable
import com.frozendroid.frozengun.models.Lobby
import com.frozendroid.frozengun.models.MinigamePlayer

class PlayerLeaveLobbyEvent(val lobby: Lobby,
                            val player: MinigamePlayer,
                            vararg messageable: Messageable): MessageEvent(*messageable)
