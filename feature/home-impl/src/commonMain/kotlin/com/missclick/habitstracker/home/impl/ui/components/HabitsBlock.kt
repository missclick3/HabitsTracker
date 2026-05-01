package com.missclick.habitstracker.home.impl.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.missclick.habitstracker.core.design.HabitsTheme
import com.missclick.habitstracker.core.model.HabitId
import com.missclick.habitstracker.home.impl.presenter.mainScreen.HomeHabitItem

@Composable
internal fun HabitsBlock(
    habits: List<HomeHabitItem>,
    onToggle: (HabitId) -> Unit,
    onIncrement: (HabitId) -> Unit,
    onDecrement: (HabitId) -> Unit,
    onOpenEdit: (HabitId) -> Unit,
    onChooseHabit: () -> Unit,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (habits.isEmpty()) {
            item { EmptyHabitCard(onChooseHabit) }
        } else {
            items(items = habits, key = { it.id }) {
                HabitCard(
                    habit = it,
                    onToggle = onToggle,
                    onIncrement = onIncrement,
                    onDecrement = onDecrement,
                    onOpenEdit = onOpenEdit,
                )
            }
        }
    }
}

@Composable
private fun HabitCard(
    habit: HomeHabitItem,
    onToggle: (HabitId) -> Unit,
    onIncrement: (HabitId) -> Unit,
    onDecrement: (HabitId) -> Unit,
    onOpenEdit: (HabitId) -> Unit,
) = Box(
    modifier =
        Modifier
            .background(color = HabitsTheme.colors.onBrand, shape = RoundedCornerShape(28.dp))
            .clickable(onClick = { onOpenEdit(habit.id) }),
) {
}

@Composable
private fun EmptyHabitCard(onChooseHabit: () -> Unit) {
}
