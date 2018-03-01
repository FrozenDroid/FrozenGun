package com.frozendroid.frozengun.events

import com.frozendroid.frozengun.interfaces.Messageable
import com.frozendroid.frozengun.models.Lobby

class LobbyCountdownEvent(messageable: Messageable,
                          val lobby: Lobby): MessageEvent(messageable) {
}