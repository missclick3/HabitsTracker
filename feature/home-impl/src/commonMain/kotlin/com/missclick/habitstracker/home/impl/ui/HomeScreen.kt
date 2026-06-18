package com.missclick.habitstracker.home.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.missclick.habitstracker.core.design.HabitsTheme
import com.missclick.habitstracker.core.model.HabitId
import com.missclick.habitstracker.core.utils.StringUtils.EMPTY_STRING
import com.missclick.habitstracker.home.impl.presenter.mainScreen.HomeHabitItem
import com.missclick.habitstracker.home.impl.presenter.mainScreen.HomeHabitProgress
import com.missclick.habitstracker.home.impl.presenter.mainScreen.HomeIntent
import com.missclick.habitstracker.home.impl.presenter.mainScreen.HomeState
import com.missclick.habitstracker.home.impl.ui.components.HeaderBlock
import com.missclick.habitstracker.home.impl.ui.components.QuoteCard
import com.missclick.habitstracker.home.impl.ui.components.ReflectionsBlock
import habitstracker.home_impl.generated.resources.Res
import habitstracker.home_impl.generated.resources.home_archive_avatar_label
import habitstracker.home_impl.generated.resources.home_binary_completed_supporting
import habitstracker.home_impl.generated.resources.home_binary_done
import habitstracker.home_impl.generated.resources.home_binary_mark
import habitstracker.home_impl.generated.resources.home_binary_pending_supporting
import habitstracker.home_impl.generated.resources.home_count_complete
import habitstracker.home_impl.generated.resources.home_count_in_progress
import habitstracker.home_impl.generated.resources.home_count_supporting
import habitstracker.home_impl.generated.resources.home_empty_body
import habitstracker.home_impl.generated.resources.home_empty_button
import habitstracker.home_impl.generated.resources.home_empty_title
import habitstracker.home_impl.generated.resources.home_habits_section_title
import habitstracker.home_impl.generated.resources.home_header_overline
import habitstracker.home_impl.generated.resources.home_header_title
import habitstracker.home_impl.generated.resources.home_new_habit_action
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
) {
    LazyColumn(
        modifier =
            modifier
                .fillMaxSize()
                .background(HabitsTheme.colors.background),
        verticalArrangement = Arrangement.spacedBy(HabitsTheme.dimensions.spacingXl),
    ) {
        item {
            HeaderBlock(
                greetingLabel = state.userName,
                date = state.dateLabel.replace(".", EMPTY_STRING),
            )
        }

        state.quote?.let { quote ->
            item { QuoteCard(quote = quote) }
        }

        item {
            ReflectionsBlock(
                state = state.reflection,
                onMoodSelected = { onIntent(HomeIntent.MoodSelected(it)) },
                onNoteChanged = { onIntent(HomeIntent.ReflectionNoteChanged(it)) },
            )
        }

        item {
            SectionHeader(
                title = stringResource(Res.string.home_habits_section_title),
                action = stringResource(Res.string.home_new_habit_action),
                onActionClick = { onIntent(HomeIntent.CreateHabitClicked) },
            )
        }

        if (state.isEmpty) {
            item {
                EmptyHabitsCard(onCreateHabit = { onIntent(HomeIntent.CreateHabitClicked) })
            }
        } else {
            items(
                count = state.habits.size,
                key = { index -> state.habits[index].id.value },
            ) { index ->
                HabitCard(
                    habit = state.habits[index],
                    onToggle = { onIntent(HomeIntent.ToggleHabit(it)) },
                    onIncrement = { onIntent(HomeIntent.IncrementHabit(it)) },
                    onDecrement = { onIntent(HomeIntent.DecrementHabit(it)) },
                    onOpenEdit = { onIntent(HomeIntent.HabitClicked(it)) },
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    action: String,
    onActionClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = HabitsTheme.dimensions.pagePaddingHorizontal),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title.uppercase(),
            style = HabitsTheme.textStyles.label,
            color = HabitsTheme.colors.textMuted,
        )
        Text(
            text = action,
            modifier = Modifier.clickable(onClick = onActionClick),
            style = HabitsTheme.textStyles.label,
            color = HabitsTheme.colors.accent,
        )
    }
}

@Composable
private fun EmptyHabitsCard(onCreateHabit: () -> Unit) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = HabitsTheme.dimensions.pagePaddingHorizontal),
        colors = CardDefaults.cardColors(containerColor = HabitsTheme.colors.surface),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(HabitsTheme.dimensions.spacingMd),
        ) {
            Text(
                text = stringResource(Res.string.home_empty_title),
                style = HabitsTheme.textStyles.bodyMedium,
                color = HabitsTheme.colors.text,
            )
            Text(
                text = stringResource(Res.string.home_empty_body),
                style = HabitsTheme.textStyles.body,
                textAlign = TextAlign.Center,
                color = HabitsTheme.colors.textMuted,
            )
            Button(
                onClick = onCreateHabit,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = HabitsTheme.colors.accent,
                        contentColor = HabitsTheme.colors.onAccent,
                    ),
            ) {
                Text(
                    text = stringResource(Res.string.home_empty_button),
                    style = HabitsTheme.textStyles.buttonLabel,
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
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        onClick = { onOpenEdit(habit.id) },
        colors = CardDefaults.cardColors(containerColor = HabitsTheme.colors.surface),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.58f)
                            .padding(end = 16.dp),
                ) {
                    Text(
                        text = habit.title,
                        style = HabitsTheme.textStyles.bodyMedium,
                        color = HabitsTheme.colors.text,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = habitSupportingText(habit.progress),
                        color = HabitsTheme.colors.textMuted,
                        style = HabitsTheme.textStyles.body,
                    )
                }

                when (val progress = habit.progress) {
                    is HomeHabitProgress.Binary ->
                        BinaryProgressBadge(
                            isCompleted = progress.isCompleted,
                            onToggle = { onToggle(habit.id) },
                        )

                    is HomeHabitProgress.Count ->
                        CountProgressControls(
                            current = progress.current,
                            target = progress.target,
                            isCompleted = progress.isCompleted,
                            onIncrement = { onIncrement(habit.id) },
                            onDecrement = { onDecrement(habit.id) },
                        )
                }
            }
        }
    }
}

@Composable
private fun BinaryProgressBadge(
    isCompleted: Boolean,
    onToggle: () -> Unit,
) {
    Surface(
        modifier =
            Modifier
                .clip(CircleShape)
                .clickable(onClick = onToggle),
        color = if (isCompleted) HabitsTheme.colors.accent else HabitsTheme.colors.accentSoft,
    ) {
        Box(
            modifier = Modifier.size(56.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text =
                    if (isCompleted) {
                        stringResource(Res.string.home_binary_done)
                    } else {
                        stringResource(Res.string.home_binary_mark)
                    },
                color = if (isCompleted) HabitsTheme.colors.onAccent else HabitsTheme.colors.accent,
                style = HabitsTheme.textStyles.label,
            )
        }
    }
}

@Composable
private fun CountProgressControls(
    current: Int,
    target: Int,
    isCompleted: Boolean,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            StepChip(label = "-", onClick = onDecrement)
            Text(
                text = "$current/$target",
                style = HabitsTheme.textStyles.metric,
                color = if (isCompleted) HabitsTheme.colors.accent else HabitsTheme.colors.textMuted,
            )
            StepChip(label = "+", onClick = onIncrement)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text =
                if (isCompleted) {
                    stringResource(Res.string.home_count_complete)
                } else {
                    stringResource(Res.string.home_count_in_progress)
                },
            style = HabitsTheme.textStyles.label,
            color = if (isCompleted) HabitsTheme.colors.accent else HabitsTheme.colors.textMuted,
        )
    }
}

@Composable
private fun StepChip(
    label: String,
    onClick: () -> Unit,
) {
    Surface(
        modifier =
            Modifier
                .size(32.dp)
                .clip(CircleShape)
                .clickable(onClick = onClick),
        color = HabitsTheme.colors.accentSoft,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                color = HabitsTheme.colors.accent,
                style = HabitsTheme.textStyles.label,
            )
        }
    }
}

@Composable
private fun habitSupportingText(progress: HomeHabitProgress): String =
    when (progress) {
        is HomeHabitProgress.Binary ->
            if (progress.isCompleted) {
                stringResource(Res.string.home_binary_completed_supporting)
            } else {
                stringResource(Res.string.home_binary_pending_supporting)
            }

        is HomeHabitProgress.Count ->
            stringResource(
                Res.string.home_count_supporting,
                progress.current,
                progress.target,
            )
    }
