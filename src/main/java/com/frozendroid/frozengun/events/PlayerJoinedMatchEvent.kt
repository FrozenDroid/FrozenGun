package com.frozendroid.frozengun.events

import com.frozendroid.frozengun.interfaces.Messageable
import com.frozendroid.frozengun.models.Match
import com.frozendroid.frozengun.models.MinigamePlayer
import com.sun.xml.internal.ws.api.message.Message

class PlayerJoinedMatchEvent(messageable: Messageable,
                             val player: MinigamePlayer,
                             val match: Match) : MessageEvent(messageable)