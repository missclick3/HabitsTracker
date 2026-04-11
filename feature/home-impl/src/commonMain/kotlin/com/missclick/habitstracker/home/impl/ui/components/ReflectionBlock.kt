package com.missclick.habitstracker.home.impl.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.missclick.habitstracker.core.design.HabitsTheme
import com.missclick.habitstracker.core.model.Mood
import com.missclick.habitstracker.home.impl.presenter.ReflectionUiState
import habitstracker.core.generated.resources.mood_drained
import habitstracker.core.generated.resources.mood_good
import habitstracker.core.generated.resources.mood_great
import habitstracker.core.generated.resources.mood_low
import habitstracker.core.generated.resources.mood_neutral
import habitstracker.home_impl.generated.resources.Res
import habitstracker.home_impl.generated.resources.home_reflection_note_placeholder
import habitstracker.home_impl.generated.resources.home_reflection_prompt
import habitstracker.home_impl.generated.resources.home_reflection_section_title
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import habitstracker.core.generated.resources.Res as CoreRes

@Composable
internal fun ReflectionsBlock(
    state: ReflectionUiState,
    onMoodSelected: (Mood) -> Unit,
    onNoteChanged: (String) -> Unit,
) = Column(
    modifier =
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
) {
    Text(
        text = stringResource(Res.string.home_reflection_section_title).uppercase(),
        style = HabitsTheme.textStyles.sectionOverline,
        color = HabitsTheme.colors.textTertiary,
        modifier = Modifier.padding(horizontal = 4.dp),
    )
    Spacer(modifier = Modifier.size(16.dp))
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(HabitsTheme.colors.surface, RoundedCornerShape(28.dp)),
    ) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(Res.string.home_reflection_prompt),
                style = HabitsTheme.textStyles.bodyPrompt,
                color = HabitsTheme.colors.textStrong,
            )
            Spacer(modifier = Modifier.size(24.dp))
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
            Spacer(modifier = Modifier.size(24.dp))
            OutlinedTextField(
                value = state.note,
                onValueChange = onNoteChanged,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                textStyle = HabitsTheme.textStyles.bodyText,
                shape = RoundedCornerShape(16.dp),
                placeholder = {
                    Text(
                        text = stringResource(Res.string.home_reflection_note_placeholder),
                        style = HabitsTheme.textStyles.bodyTextMuted,
                        color = HabitsTheme.colors.textHint,
                    )
                },
                colors =
                    OutlinedTextFieldDefaults.colors(
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
}

@Composable
private fun MoodChip(
    mood: Mood,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val selectedBackground = if (isSelected) mood.toIconColor() else mood.toIconColor().copy(alpha = 0.1f)
    val content = if (isSelected) Color.Black else HabitsTheme.colors.textInactive

    Surface(
        modifier =
            Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable(onClick = onClick),
        color = selectedBackground,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(mood.toIconResource()),
                contentDescription = null,
                tint = content,
                modifier = Modifier.size(48.dp),
            )
        }
    }
}

private fun Mood.toIconResource(): DrawableResource =
    when (this) {
        Mood.Drained -> CoreRes.drawable.mood_drained
        Mood.Low -> CoreRes.drawable.mood_low
        Mood.Neutral -> CoreRes.drawable.mood_neutral
        Mood.Good -> CoreRes.drawable.mood_good
        Mood.Great -> CoreRes.drawable.mood_great
    }

@Composable
private fun Mood.toIconColor() =
    when (this) {
        Mood.Drained -> HabitsTheme.colors.moodDrained
        Mood.Low -> HabitsTheme.colors.moodLow
        Mood.Neutral -> HabitsTheme.colors.moodNeutral
        Mood.Good -> HabitsTheme.colors.moodGood
        Mood.Great -> HabitsTheme.colors.moodGreat
    }
