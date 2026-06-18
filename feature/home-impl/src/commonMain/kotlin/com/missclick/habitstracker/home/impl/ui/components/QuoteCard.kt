package com.missclick.habitstracker.home.impl.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.missclick.habitstracker.core.design.HabitsTheme
import com.missclick.habitstracker.home.impl.presenter.mainScreen.QuoteUiState

@Composable
internal fun QuoteCard(
    quote: QuoteUiState,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(16.dp),
        color = HabitsTheme.colors.surface,
        border = BorderStroke(1.dp, HabitsTheme.colors.border),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = "“${quote.text}”",
                style = HabitsTheme.textStyles.body,
                color = HabitsTheme.colors.text,
            )
            Text(
                text = "— ${quote.author}",
                style = HabitsTheme.textStyles.label,
                color = HabitsTheme.colors.textMuted,
            )
        }
    }
}