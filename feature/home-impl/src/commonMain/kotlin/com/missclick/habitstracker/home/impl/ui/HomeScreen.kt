package com.missclick.habitstracker.home.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.missclick.habitstracker.core.model.Mood
import com.missclick.habitstracker.home.impl.presenter.HomeHabitItem
import com.missclick.habitstracker.home.impl.presenter.HomeHabitProgress
import com.missclick.habitstracker.home.impl.presenter.HomeIntent
import com.missclick.habitstracker.home.impl.presenter.HomeState
import com.missclick.habitstracker.home.impl.presenter.ReflectionUiState
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
import habitstracker.home_impl.generated.resources.home_reflection_note_placeholder
import habitstracker.home_impl.generated.resources.home_reflection_prompt
import habitstracker.home_impl.generated.resources.home_reflection_section_title
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(HabitsTheme.colors.background),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            HomeHeader(
                greetingLabel = state.greetingLabel,
                dateLabel = state.dateLabel,
                onOpenArchive = { onIntent(HomeIntent.ArchiveClicked) },
            )
        }

        item {
            ReflectionCard(
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
private fun HomeHeader(
    greetingLabel: String?,
    dateLabel: String,
    onOpenArchive: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = (greetingLabel ?: stringResource(Res.string.home_header_overline)).uppercase(),
                style = HabitsTheme.textStyles.sectionLabel,
                color = HabitsTheme.colors.textTertiary,
            )
            Text(
                text = stringResource(Res.string.home_header_title),
                style = HabitsTheme.textStyles.screenTitle,
                color = HabitsTheme.colors.textPrimary,
            )
            Text(
                text = dateLabel,
                style = HabitsTheme.textStyles.screenSubtitle,
                color = HabitsTheme.colors.textSecondary,
            )
        }

        Surface(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .clickable(onClick = onOpenArchive),
            color = HabitsTheme.colors.surface,
            shadowElevation = 6.dp,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(Res.string.home_archive_avatar_label),
                    color = HabitsTheme.colors.brandPrimary,
                    style = HabitsTheme.textStyles.chipLabel,
                )
            }
        }
    }
}

@Composable
private fun ReflectionCard(
    state: ReflectionUiState,
    onMoodSelected: (Mood) -> Unit,
    onNoteChanged: (String) -> Unit,
) {
    SectionCard(
        title = stringResource(Res.string.home_reflection_section_title),
        modifier = Modifier.padding(horizontal = 20.dp),
    ) {
        Text(
            text = stringResource(Res.string.home_reflection_prompt),
            style = HabitsTheme.textStyles.cardTitle,
            color = HabitsTheme.colors.textStrong,
        )
        Spacer(modifier = Modifier.height(18.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Mood.entries.forEach { mood ->
                MoodChip(
                    mood = mood,
                    isSelected = state.selectedMood == mood,
                    onClick = { onMoodSelected(mood) },
                )
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        OutlinedTextField(
            value = state.note,
            onValueChange = onNoteChanged,
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            textStyle = HabitsTheme.textStyles.body,
            shape = RoundedCornerShape(20.dp),
            placeholder = {
                Text(
                    text = stringResource(Res.string.home_reflection_note_placeholder),
                    style = HabitsTheme.textStyles.bodyMuted,
                    color = HabitsTheme.colors.textHint,
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = HabitsTheme.colors.surfaceSubtle,
                unfocusedContainerColor = HabitsTheme.colors.surfaceSubtle,
                focusedTextColor = HabitsTheme.colors.textPrimary,
                unfocusedTextColor = HabitsTheme.colors.textPrimary,
                focusedBorderColor = HabitsTheme.colors.brandPrimary,
                unfocusedBorderColor = HabitsTheme.colors.border,
                cursorColor = HabitsTheme.colors.brandPrimary,
                focusedPlaceholderColor = HabitsTheme.colors.textHint,
                unfocusedPlaceholderColor = HabitsTheme.colors.textHint,
            ),
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    action: String,
    onActionClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title.uppercase(),
            style = HabitsTheme.textStyles.sectionLabel,
            color = HabitsTheme.colors.textTertiary,
        )
        Text(
            text = action,
            modifier = Modifier.clickable(onClick = onActionClick),
            style = HabitsTheme.textStyles.actionLabel,
            color = HabitsTheme.colors.brandPrimary,
        )
    }
}

@Composable
private fun EmptyHabitsCard(
    onCreateHabit: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        colors = CardDefaults.cardColors(containerColor = HabitsTheme.colors.surface),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(Res.string.home_empty_title),
                style = HabitsTheme.textStyles.cardTitle,
                color = HabitsTheme.colors.textStrong,
            )
            Text(
                text = stringResource(Res.string.home_empty_body),
                style = HabitsTheme.textStyles.bodyMuted,
                textAlign = TextAlign.Center,
                color = HabitsTheme.colors.textMuted,
            )
            Button(
                onClick = onCreateHabit,
                colors = ButtonDefaults.buttonColors(
                    containerColor = HabitsTheme.colors.brandPrimary,
                    contentColor = HabitsTheme.colors.onBrand,
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        onClick = { onOpenEdit(habit.id) },
        colors = CardDefaults.cardColors(containerColor = HabitsTheme.colors.surface),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Column(
            modifier = Modifier
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
                    modifier = Modifier
                        .fillMaxWidth(0.58f)
                        .padding(end = 16.dp),
                ) {
                    Text(
                        text = habit.title,
                        style = HabitsTheme.textStyles.cardTitle,
                        color = HabitsTheme.colors.textStrong,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = habitSupportingText(habit.progress),
                        color = HabitsTheme.colors.textMuted,
                        style = HabitsTheme.textStyles.supporting,
                    )
                }

                when (val progress = habit.progress) {
                    is HomeHabitProgress.Binary -> BinaryProgressBadge(
                        isCompleted = progress.isCompleted,
                        onToggle = { onToggle(habit.id) },
                    )

                    is HomeHabitProgress.Count -> CountProgressControls(
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
        modifier = Modifier
            .clip(CircleShape)
            .clickable(onClick = onToggle),
        color = if (isCompleted) HabitsTheme.colors.brandPrimary else HabitsTheme.colors.brandPrimarySoft,
    ) {
        Box(
            modifier = Modifier.size(56.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = if (isCompleted) {
                    stringResource(Res.string.home_binary_done)
                } else {
                    stringResource(Res.string.home_binary_mark)
                },
                color = if (isCompleted) HabitsTheme.colors.onBrand else HabitsTheme.colors.brandPrimary,
                style = HabitsTheme.textStyles.actionLabel,
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
                style = HabitsTheme.textStyles.metricValue,
                color = if (isCompleted) HabitsTheme.colors.success else HabitsTheme.colors.textSecondary,
            )
            StepChip(label = "+", onClick = onIncrement)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (isCompleted) {
                stringResource(Res.string.home_count_complete)
            } else {
                stringResource(Res.string.home_count_in_progress)
            },
            style = HabitsTheme.textStyles.actionLabel,
            color = if (isCompleted) HabitsTheme.colors.success else HabitsTheme.colors.textTertiary,
        )
    }
}

@Composable
private fun StepChip(
    label: String,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        color = HabitsTheme.colors.brandPrimarySoft,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                color = HabitsTheme.colors.brandPrimary,
                style = HabitsTheme.textStyles.chipLabel,
            )
        }
    }
}

@Composable
private fun habitSupportingText(progress: HomeHabitProgress): String = when (progress) {
    is HomeHabitProgress.Binary -> if (progress.isCompleted) {
        stringResource(Res.string.home_binary_completed_supporting)
    } else {
        stringResource(Res.string.home_binary_pending_supporting)
    }

    is HomeHabitProgress.Count -> stringResource(
        Res.string.home_count_supporting,
        progress.current,
        progress.target,
    )
}

@Composable
private fun MoodChip(
    mood: Mood,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val background = if (isSelected) HabitsTheme.colors.brandPrimarySoftSelected else HabitsTheme.colors.surfaceSubtle
    val content = if (isSelected) HabitsTheme.colors.brandPrimary else HabitsTheme.colors.textInactive

    Surface(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        color = background,
        shadowElevation = if (isSelected) 4.dp else 0.dp,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = mood.toFace(),
                color = content,
                style = HabitsTheme.textStyles.chipLabel,
            )
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = title.uppercase(),
            style = HabitsTheme.textStyles.sectionLabel,
            color = HabitsTheme.colors.textTertiary,
            modifier = Modifier.padding(horizontal = 4.dp),
        )
        Card(
            colors = CardDefaults.cardColors(containerColor = HabitsTheme.colors.surface),
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
            ) {
                content()
            }
        }
    }
}

private fun Mood.toFace(): String = when (this) {
    Mood.Drained -> ":("
    Mood.Low -> ":-|"
    Mood.Neutral -> ":|"
    Mood.Good -> ":)"
    Mood.Great -> ":D"
}
