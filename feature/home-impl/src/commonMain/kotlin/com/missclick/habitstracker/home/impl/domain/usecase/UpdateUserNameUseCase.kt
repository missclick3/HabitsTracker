package com.missclick.habitstracker.home.impl.domain.usecase

import com.missclick.habitstracker.home.impl.domain.repository.IUserRepository

internal class UpdateUserNameUseCase(private val repository: IUserRepository) {
    suspend operator fun invoke(name: String) = repository.updateUserName(name)
}