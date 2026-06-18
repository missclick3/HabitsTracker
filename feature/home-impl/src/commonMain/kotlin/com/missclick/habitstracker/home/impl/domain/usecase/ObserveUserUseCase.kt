package com.missclick.habitstracker.home.impl.domain.usecase

import com.missclick.habitstracker.home.impl.domain.model.User
import com.missclick.habitstracker.home.impl.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow

internal class ObserveUserUseCase(private val repository: IUserRepository) {
    operator fun invoke(): Flow<User?> = repository.observeUser()
}