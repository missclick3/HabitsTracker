package com.missclick.habitstracker.home.impl.presenter.editHabit

import com.missclick.habitstracker.core.model.HabitKind
import com.missclick.habitstracker.core.utils.StringUtils.EMPTY_STRING

internal sealed interface EditHabitIntent {
    data class TitleChanged(val title: String) : EditHabitIntent
    data class KindChanged(val kind: HabitKind) : EditHabitIntent
    data class TargetCountChanged(val count: String) : EditHabitIntent
    data object SaveClicked : EditHabitIntent
    data object DeleteClicked : EditHabitIntent
}

internal data class EditHabitState(
    val isCreateMode: Boolean,
    val habitId: String,
    val title: String,
    val kind: HabitKind,
    val targetCountInput: String,
    val isSaving: Boolean,
) {
    companion object {
        fun default(habitId: String?) = EditHabitState(
            isCreateMode = habitId == null,
            habitId = habitId ?: EMPTY_STRING,
            title = EMPTY_STRING,
            kind = HabitKind.Binary,
            targetCountInput = EMPTY_STRING,
            isSaving = false,
        )
    }
}