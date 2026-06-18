package com.missclick.habitstracker.home.impl.domain.usecase

import com.missclick.habitstracker.home.impl.data.repository.RoomHomeRepository
import com.missclick.habitstracker.home.impl.data.repository.RoomUserRepository

internal class SeedDatabaseUseCase(
    private val homeRepository: RoomHomeRepository,
    private val userRepository: RoomUserRepository,
) {
    suspend operator fun invoke() {
        homeRepository.seedIfEmpty()
        userRepository.seedIfAbsent()
    }
}