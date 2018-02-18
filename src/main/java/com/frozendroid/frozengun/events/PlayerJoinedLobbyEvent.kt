package com.frozendroid.frozengun.events

import com.frozendroid.frozengun.interfaces.Messageable
import com.frozendroid.frozengun.models.Lobby
import com.frozendroid.frozengun.models.MinigamePlayer

class PlayerJoinedLobbyEvent(messageable: Messageable,
                             val lobby: Lobby,
                             val player: MinigamePlayer): MessageEvent(messageable)