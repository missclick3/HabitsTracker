package com.missclick.habitstracker.home.impl.presenter.editHabit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.missclick.habitstracker.core.model.HabitId
import com.missclick.habitstracker.core.model.HabitKind
import com.missclick.habitstracker.core.navigation.Navigator
import com.missclick.habitstracker.home.impl.domain.model.Habit
import com.missclick.habitstracker.home.impl.domain.usecase.CreateHabitUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.DeleteHabitUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.GetHabitByIdUseCase
import com.missclick.habitstracker.home.impl.domain.usecase.UpdateHabitUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal class EditHabitViewModel(
    private val habitId: String?,
    private val navigator: Navigator,
    private val getHabitById: GetHabitByIdUseCase,
    private val createHabit: CreateHabitUseCase,
    private val updateHabit: UpdateHabitUseCase,
    private val deleteHabit: DeleteHabitUseCase,
) : ViewModel() {

    private val mutableState = MutableStateFlow(EditHabitState.default(habitId))

    val state: StateFlow<EditHabitState> = mutableState.asStateFlow()

    init {
        if (habitId != null) {
            viewModelScope.launch { loadHabit() }
        }
    }

    fun onIntent(intent: EditHabitIntent) {
        when (intent) {
            is EditHabitIntent.TitleChanged ->
                mutableState.value = mutableState.value.copy(title = intent.title)

            is EditHabitIntent.KindChanged ->
                mutableState.value = mutableState.value.copy(
                    kind = intent.kind,
                    targetCountInput = if (intent.kind == HabitKind.Binary) "" else mutableState.value.targetCountInput,
                )

            is EditHabitIntent.TargetCountChanged ->
                mutableState.value = mutableState.value.copy(targetCountInput = intent.count)

            EditHabitIntent.SaveClicked -> save()

            EditHabitIntent.DeleteClicked -> delete()
        }
    }

    private suspend fun loadHabit() {
        val habit = getHabitById(HabitId(habitId!!)) ?: return
        mutableState.value = mutableState.value.copy(
            title = habit.title,
            kind = habit.kind,
            targetCountInput = habit.targetCount?.toString() ?: "",
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun save() {
        val current = mutableState.value
        mutableState.value = current.copy(isSaving = true)
        viewModelScope.launch {
            val habit = Habit(
                id = HabitId(if (current.isCreateMode) Uuid.random().toString() else current.habitId),
                title = current.title,
                kind = current.kind,
                targetCount = if (current.kind == HabitKind.Count) current.targetCountInput.toIntOrNull() else null,
            )
            if (current.isCreateMode) createHabit(habit) else updateHabit(habit)
            navigator.back()
        }
    }

    private fun delete() {
        viewModelScope.launch {
            deleteHabit(HabitId(habitId!!))
            navigator.back()
        }
    }
}