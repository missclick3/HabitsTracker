package com.missclick.habitstracker.home.impl.di

import com.missclick.habitstracker.core.navigation.FeatureEntryBuilder
import com.missclick.habitstracker.home.impl.data.network.QuoteApiDataSource
import com.missclick.habitstracker.home.impl.data.repository.QuoteRepository
import com.missclick.habitstracker.home.impl.data.repository.RoomHomeRepository
import com.missclick.habitstracker.home.impl.data.repository.RoomUserRepository
import com.missclick.habitstracker.home.impl.domain.mapper.HomeStateMapper
import com.missclick.habitstracker.home.impl.domain.repository.IHomeRepository
import com.missclick.habitstracker.home.impl.domain.repository.IUserRepository
import com.missclick.habitstracker.home.impl.domain.usecase.CreateHabitUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.DateProvider
import com.missclick.habitstracker.home.impl.domain.usecase.GetQuoteOfDayUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.DeleteHabitUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.DecrementHabitUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.GetHabitByIdUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.GetTodayDateLabelUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.IncrementHabitUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.LocaleDateFormatter
import com.missclick.habitstracker.home.impl.domain.usecase.ObserveHomeUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.ObserveUserUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.SeedDatabaseUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.SystemDateProvider
import com.missclick.habitstracker.home.impl.domain.usecase.ToggleHabitUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.UpdateHabitUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.UpdateReflectionMoodUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.UpdateReflectionNoteUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.UpdateUserNameUseCase
import com.missclick.habitstracker.home.impl.navigation.homeEntries
import com.missclick.habitstracker.home.impl.presenter.editHabit.EditHabitViewModel
import com.missclick.habitstracker.home.impl.presenter.mainScreen.HomeViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val homeFeatureModule: Module =
    module {
        single {
            HttpClient {
                install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
            }
        }
        single { QuoteApiDataSource(client = get(), apiKey = get(named("quoteApiKey"))) }
        single { QuoteRepository(dao = get(), dataSource = get()) }
        single { GetQuoteOfDayUseCase(repo = get(), dateProvider = get()) }

        single<DateProvider> { SystemDateProvider() }

        single {
            RoomUserRepository(userDao = get())
        }
        single<IUserRepository> { get<RoomUserRepository>() }

        single {
            RoomHomeRepository(
                habitDao = get(),
                recordDao = get(),
                reflectionDao = get(),
                userRepository = get<RoomUserRepository>(),
                todayDate = { get<DateProvider>().today().toString() },
            )
        }
        single<IHomeRepository> { get<RoomHomeRepository>() }

        single { HomeStateMapper() }
        single { LocaleDateFormatter() }
        single { ObserveHomeUseCase(get(), get()) }
        single { ToggleHabitUseCase(get()) }
        single { IncrementHabitUseCase(get()) }
        single { DecrementHabitUseCase(get()) }
        single { UpdateReflectionMoodUseCase(get()) }
        single { UpdateReflectionNoteUseCase(get()) }
        single { GetTodayDateLabelUseCase(get(), get()) }
        single { CreateHabitUseCase(get()) }
        single { UpdateHabitUseCase(get()) }
        single { DeleteHabitUseCase(get()) }
        single { GetHabitByIdUseCase(get()) }
        single { ObserveUserUseCase(get()) }
        single { UpdateUserNameUseCase(get()) }
        single { SeedDatabaseUseCase(get(), get()) }

        viewModel {
            HomeViewModel(
                observeHome = get(),
                toggleHabit = get(),
                incrementHabit = get(),
                decrementHabit = get(),
                updateReflectionMood = get(),
                updateReflectionNote = get(),
                getTodayDateLabelUseCase = get(),
                navigator = get(),
                seedDatabase = get(),
                getQuoteOfDay = get(),
            )
        }
        viewModel { params ->
            EditHabitViewModel(
                habitId = params.getOrNull(),
                navigator = get(),
                getHabitById = get(),
                createHabit = get(),
                updateHabit = get(),
                deleteHabit = get(),
            )
        }

        single<FeatureEntryBuilder>(qualifier = named("home")) {
            { homeEntries() }
        }
    }