package com.missclick.habitstracker.home.impl.presenter.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.missclick.habitstracker.core.navigation.Navigator
import com.missclick.habitstracker.home.api.HomeScreenRoute
import com.missclick.habitstracker.home.impl.domain.usecase.DecrementHabitUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.GetTodayDateLabelUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.IncrementHabitUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.ObserveHomeUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.SeedDatabaseUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.ToggleHabitUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.GetQuoteOfDayUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.UpdateReflectionMoodUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.UpdateReflectionNoteUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class HomeViewModel(
    private val observeHome: ObserveHomeUseCase,
    private val toggleHabit: ToggleHabitUseCase,
    private val incrementHabit: IncrementHabitUseCase,
    private val decrementHabit: DecrementHabitUseCase,
    private val updateReflectionMood: UpdateReflectionMoodUseCase,
    private val updateReflectionNote: UpdateReflectionNoteUseCase,
    private val getTodayDateLabelUseCase: GetTodayDateLabelUseCase,
    private val navigator: Navigator,
    private val seedDatabase: SeedDatabaseUseCase,
    private val getQuoteOfDay: GetQuoteOfDayUseCase,
) : ViewModel() {
    private val mutableState = MutableStateFlow(HomeState.default())
    private val mutableEffects = MutableSharedFlow<HomeEffect>()

    val state: StateFlow<HomeState> = mutableState.asStateFlow()
    val effects: SharedFlow<HomeEffect> = mutableEffects.asSharedFlow()

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Load -> load()
            HomeIntent.ArchiveClicked -> emitEffect(HomeEffect.OpenArchive)
            HomeIntent.CreateHabitClicked -> navigator.navigate(HomeScreenRoute.EditHabitScreen(null))
            is HomeIntent.HabitClicked -> navigator.navigate(HomeScreenRoute.EditHabitScreen(intent.habitId.value))
            is HomeIntent.ToggleHabit -> viewModelScope.launch { toggleHabit(intent.habitId) }
            is HomeIntent.IncrementHabit -> viewModelScope.launch { incrementHabit(intent.habitId) }
            is HomeIntent.DecrementHabit -> viewModelScope.launch { decrementHabit(intent.habitId) }
            is HomeIntent.MoodSelected -> {
                mutableState.update { current ->
                    current.copy(reflection = current.reflection.copy(selectedMood = intent.mood))
                }
                viewModelScope.launch { updateReflectionMood(intent.mood) }
            }
            is HomeIntent.ReflectionNoteChanged -> viewModelScope.launch {
                updateReflectionNote(intent.text)
            }
        }
    }

    private fun load() {
        viewModelScope.launch { seedDatabase() }
        viewModelScope.launch {
            getQuoteOfDay()?.let { quote ->
                mutableState.update { it.copy(quote = QuoteUiState(quote.text, quote.author)) }
            }
        }
        observeHome().onEach { homeState ->
            mutableState.update { current ->
                homeState.copy(
                    dateLabel = getTodayDateLabelUseCase(),
                    quote = current.quote,
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun emitEffect(effect: HomeEffect) = viewModelScope.launch {
        mutableEffects.emit(effect)
    }
}