package com.missclick.habitstracker.home.impl.domain.usecase

import com.missclick.habitstracker.home.impl.domain.mapper.HomeStateMapper
import com.missclick.habitstracker.home.impl.domain.repository.IHomeRepository
import com.missclick.habitstracker.home.impl.presenter.HomeState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ObserveHomeUseCase(
    private val repository: IHomeRepository,
    private val mapper: HomeStateMapper,
) {
    operator fun invoke(): Flow<HomeState> = repository.observeHome().map(mapper::map)
}
