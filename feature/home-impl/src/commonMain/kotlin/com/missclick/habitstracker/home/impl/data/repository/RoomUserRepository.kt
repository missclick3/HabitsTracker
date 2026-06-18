package com.missclick.habitstracker.home.impl.data.repository

import com.missclick.habitstracker.database.dao.UserDao
import com.missclick.habitstracker.database.entity.UserEntity
import com.missclick.habitstracker.home.impl.domain.model.User
import com.missclick.habitstracker.home.impl.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class RoomUserRepository(
    private val userDao: UserDao,
) : IUserRepository {

    override fun observeUser(): Flow<User?> =
        userDao.observeUser().map { entity -> entity?.let { User(name = it.name) } }

    override suspend fun updateUserName(name: String) {
        userDao.update(UserEntity(id = 1, name = name))
    }

    suspend fun seedIfAbsent() {
        userDao.insertIfAbsent(UserEntity(id = 1, name = "You"))
    }
}