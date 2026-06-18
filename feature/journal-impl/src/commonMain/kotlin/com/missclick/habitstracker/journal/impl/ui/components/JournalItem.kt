package com.missclick.habitstracker.journal.impl.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.missclick.habitstracker.core.design.HabitsTheme
import com.missclick.habitstracker.core.model.Mood
import com.missclick.habitstracker.journal.impl.presenter.journalList.JournalEntryItem
import habitstracker.journal_impl.generated.resources.Res
import habitstracker.journal_impl.generated.resources.journal_mood_drained
import habitstracker.journal_impl.generated.resources.journal_mood_good
import habitstracker.journal_impl.generated.resources.journal_mood_great
import habitstracker.journal_impl.generated.resources.journal_mood_low
import habitstracker.journal_impl.generated.resources.journal_mood_neutral
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import habitstracker.core.generated.resources.Res as CoreRes
import habitstracker.core.generated.resources.mood_drained
import habitstracker.core.generated.resources.mood_good
import habitstracker.core.generated.resources.mood_great
import habitstracker.core.generated.resources.mood_low
import habitstracker.core.generated.resources.mood_neutral

@Composable
internal fun JournalItem(
    entry: JournalEntryItem,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = HabitsTheme.colors.surface,
        border = BorderStroke(1.dp, HabitsTheme.colors.border),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = entry.dateLabel,
                style = HabitsTheme.textStyles.body,
                fontWeight = FontWeight.SemiBold,
                color = HabitsTheme.colors.text,
            )
            entry.mood?.let { mood ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Surface(
                        shape = CircleShape,
                        color = mood.toColor(),
                        modifier = Modifier.size(32.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(mood.toIconResource()),
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(32.dp),
                            )
                        }
                    }
                    Text(
                        text = stringResource(mood.toStringRes()),
                        style = HabitsTheme.textStyles.body,
                        fontWeight = FontWeight.SemiBold,
                        color = mood.toColor(),
                    )
                }
            }
            if (entry.note.isNotBlank()) {
                Text(
                    text = entry.note,
                    style = HabitsTheme.textStyles.body,
                    color = HabitsTheme.colors.textFaint,
                )
            }
        }
    }
}

@Composable
private fun Mood.toColor(): Color = when (this) {
    Mood.Drained -> HabitsTheme.colors.moodAwful
    Mood.Low -> HabitsTheme.colors.moodBad
    Mood.Neutral -> HabitsTheme.colors.moodOkay
    Mood.Good -> HabitsTheme.colors.moodGood
    Mood.Great -> HabitsTheme.colors.moodGreat
}

private fun Mood.toIconResource(): DrawableResource = when (this) {
    Mood.Drained -> CoreRes.drawable.mood_drained
    Mood.Low -> CoreRes.drawable.mood_low
    Mood.Neutral -> CoreRes.drawable.mood_neutral
    Mood.Good -> CoreRes.drawable.mood_good
    Mood.Great -> CoreRes.drawable.mood_great
}

private fun Mood.toStringRes(): StringResource = when (this) {
    Mood.Drained -> Res.string.journal_mood_drained
    Mood.Low -> Res.string.journal_mood_low
    Mood.Neutral -> Res.string.journal_mood_neutral
    Mood.Good -> Res.string.journal_mood_good
    Mood.Great -> Res.string.journal_mood_great
}