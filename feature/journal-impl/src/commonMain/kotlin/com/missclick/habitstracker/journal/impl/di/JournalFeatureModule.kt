package com.missclick.habitstracker.journal.impl.di

import com.missclick.habitstracker.core.navigation.FeatureEntryBuilder
import com.missclick.habitstracker.journal.impl.data.repository.RoomJournalRepository
import com.missclick.habitstracker.journal.impl.domain.repository.IJournalRepository
import com.missclick.habitstracker.journal.impl.domain.usecase.ObserveJournalUseCase
import com.missclick.habitstracker.journal.impl.domain.usecase.SeedJournalUseCase
import com.missclick.habitstracker.journal.impl.navigation.journalEntries
import com.missclick.habitstracker.journal.impl.presenter.journalList.JournalViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val journalFeatureModule: Module = module {
    single { RoomJournalRepository(get()) }
    single<IJournalRepository> { get<RoomJournalRepository>() }
    single { ObserveJournalUseCase(get()) }
    single { SeedJournalUseCase(get()) }
    viewModel { JournalViewModel(get(), get()) }
    single<FeatureEntryBuilder>(qualifier = named("journal")) { { journalEntries() } }
}