package com.example.cher_g42.domain.player

data class UserInfo(val name: String) {
    init {
        require(name.isNotBlank() && name.length <= 30)
    }
}