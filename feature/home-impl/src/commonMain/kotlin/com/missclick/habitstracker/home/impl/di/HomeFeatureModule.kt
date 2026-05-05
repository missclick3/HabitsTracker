package com.missclick.habitstracker.home.impl.di

import com.missclick.habitstracker.core.navigation.FeatureEntryBuilder
import com.missclick.habitstracker.home.impl.data.repository.InMemoryHomeRepository
import com.missclick.habitstracker.home.impl.domain.mapper.HomeStateMapper
import com.missclick.habitstracker.home.impl.domain.repository.IHomeRepository
import com.missclick.habitstracker.home.impl.domain.usecase.DateProvider
import com.missclick.habitstracker.home.impl.domain.usecase.DecrementHabitUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.GetTodayDateLabelUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.IncrementHabitUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.LocaleDateFormatter
import com.missclick.habitstracker.home.impl.domain.usecase.ObserveHomeUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.SystemDateProvider
import com.missclick.habitstracker.home.impl.domain.usecase.ToggleHabitUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.UpdateReflectionMoodUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.UpdateReflectionNoteUseCase
import com.missclick.habitstracker.home.impl.navigation.homeEntries
import com.missclick.habitstracker.home.impl.presenter.mainScreen.HomeViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val homeFeatureModule: Module =
    module {
        single<IHomeRepository> { InMemoryHomeRepository() }
        single { HomeStateMapper() }
        single { LocaleDateFormatter() }
        single<DateProvider> { SystemDateProvider() }
        single { ObserveHomeUseCase(get(), get()) }
        single { ToggleHabitUseCase(get()) }
        single { IncrementHabitUseCase(get()) }
        single { DecrementHabitUseCase(get()) }
        single { UpdateReflectionMoodUseCase(get()) }
        single { UpdateReflectionNoteUseCase(get()) }
        single { GetTodayDateLabelUseCase(get(), get()) }
        viewModel { HomeViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }

        single<FeatureEntryBuilder>(qualifier = named("home")) {
            { homeEntries() }
        }
    }
