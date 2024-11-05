package com.example.cher_g42.domain.game

import com.example.cher_g42.domain.player.UserInfo
import java.util.UUID

/**
 * Data type that characterizes the player information while he's in the lobby
 * @property info   The information entered by the user
 * @property id     An identifier used to distinguish players in the lobby
 */
data class PlayerInfo(val info: UserInfo, val id: UUID = UUID.randomUUID())