package com.missclick.habitstracker.home.impl.di

import com.missclick.habitstracker.home.api.HomeFeatureApi
import com.missclick.habitstracker.home.impl.domain.repository.IHomeRepository
import com.missclick.habitstracker.home.impl.data.repository.InMemoryHomeRepository
import com.missclick.habitstracker.home.impl.navigation.HomeFeatureImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val homeFeatureModule: Module = module {
    single<IHomeRepository> { InMemoryHomeRepository() }
    single<HomeFeatureApi> { HomeFeatureImpl(repository = get()) }
}
