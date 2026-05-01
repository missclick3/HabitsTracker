package com.missclick.habitstracker.home.impl.presenter.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.missclick.habitstracker.home.impl.domain.usecase.DecrementHabitUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.GetTodayDateLabelUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.IncrementHabitUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.ObserveHomeUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.ToggleHabitUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.UpdateReflectionMoodUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.UpdateReflectionNoteUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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
) : ViewModel() {
    private val mutableState = MutableStateFlow(HomeState.default())
    private val mutableEffects = MutableSharedFlow<HomeEffect>()
    private var observeJob: Job? = null
    private var noteSaveJob: Job? = null

    val state: StateFlow<HomeState> = mutableState.asStateFlow()
    val effects: SharedFlow<HomeEffect> = mutableEffects.asSharedFlow()

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Load -> load()
            HomeIntent.ArchiveClicked -> emitEffect(HomeEffect.OpenArchive)
            HomeIntent.CreateHabitClicked -> emitEffect(HomeEffect.OpenCreateHabit)
            is HomeIntent.HabitClicked -> emitEffect(HomeEffect.OpenEditHabit(intent.habitId))
            is HomeIntent.ToggleHabit -> launch { toggleHabit(intent.habitId) }
            is HomeIntent.IncrementHabit -> launch { incrementHabit(intent.habitId) }
            is HomeIntent.DecrementHabit -> launch { decrementHabit(intent.habitId) }
            is HomeIntent.MoodSelected -> {
                mutableState.update { current ->
                    current.copy(reflection = current.reflection.copy(selectedMood = intent.mood))
                }
                launch { updateReflectionMood(intent.mood) }
            }
            is HomeIntent.ReflectionNoteChanged -> {
                mutableState.update { current ->
                    current.copy(reflection = current.reflection.copy(note = intent.text))
                }
                noteSaveJob?.cancel()
                noteSaveJob =
                    launch {
                        delay(NOTE_AUTOSAVE_DELAY_MS)
                        updateReflectionNote(intent.text)
                    }
            }
        }
    }

    private fun load() {
        if (observeJob != null) {
            return
        }

        observeJob =
            launch {
                observeHome().collect { homeState ->
                    mutableState.value =
                        homeState.copy(
                            dateLabel = getTodayDateLabelUseCase(),
                        )
                }
            }
    }

    private fun emitEffect(effect: HomeEffect) {
        launch {
            mutableEffects.emit(effect)
        }
    }

    private fun launch(block: suspend CoroutineScope.() -> Unit): Job = viewModelScope.launch(block = block)

    private companion object {
        const val NOTE_AUTOSAVE_DELAY_MS = 500L
    }
}
