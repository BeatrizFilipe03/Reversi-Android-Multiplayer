package com.example.cher_g42.domain.game

import com.example.cher_g42.domain.player.UserInfo
import org.junit.Assert.assertThrows
import org.junit.Test

class PlayerInfoTests {
    @Test
    fun validPlayerInfo(){
        val playerInfo = PlayerInfo(UserInfo("JohnDoe"))
        assert(playerInfo.info.name == "JohnDoe")
    }
    @Test
    fun invalidPlayerInfo(){
        assertThrows(IllegalArgumentException::class.java) {
            PlayerInfo(UserInfo(""))
        }
        assertThrows(IllegalArgumentException::class.java) {
            PlayerInfo(UserInfo("a".repeat(31)))
        }
    }
}