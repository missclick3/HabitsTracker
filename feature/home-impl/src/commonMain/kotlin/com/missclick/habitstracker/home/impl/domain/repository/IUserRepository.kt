package com.missclick.habitstracker.home.impl.domain.repository

import com.missclick.habitstracker.home.impl.domain.model.User
import kotlinx.coroutines.flow.Flow

internal interface IUserRepository {
    fun observeUser(): Flow<User?>

    suspend fun updateUserName(name: String)
}