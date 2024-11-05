package com.example.cher_g42.domain.player

interface UserInfoRepository {
    suspend fun getUserInfo(): UserInfo?
    suspend fun updateUserInfo(userInfo: UserInfo)
}