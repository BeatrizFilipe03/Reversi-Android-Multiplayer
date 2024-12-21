package com.example.cher_g42.domain.player

import org.junit.Assert.assertThrows
import org.junit.Test


class UserInfoTests {
    @Test
    fun validUserInfo(){
        val userInfo = UserInfo("JohnDoe")
        assert(userInfo.name == "JohnDoe")
    }
    @Test
    fun blankNameUserInfo() {
        assertThrows(IllegalArgumentException::class.java) {
            UserInfo("")
        }
    }
    @Test
    fun moreThan30CharactersNameUserInfo() {
        assertThrows(IllegalArgumentException::class.java) {
            UserInfo("a".repeat(31))
        }
    }
}