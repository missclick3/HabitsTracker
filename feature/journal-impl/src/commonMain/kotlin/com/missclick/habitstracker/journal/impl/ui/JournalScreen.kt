package com.missclick.habitstracker.journal.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.missclick.habitstracker.core.design.HabitsTheme
import com.missclick.habitstracker.journal.impl.presenter.journalList.JournalState
import com.missclick.habitstracker.journal.impl.ui.components.JournalItem
import habitstracker.journal_impl.generated.resources.Res
import habitstracker.journal_impl.generated.resources.journal_empty_body
import habitstracker.journal_impl.generated.resources.journal_empty_title
import habitstracker.journal_impl.generated.resources.journal_title
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun JournalScreen(
    state: JournalState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HabitsTheme.colors.background),
    ) {
        Text(
            text = stringResource(Res.string.journal_title),
            style = HabitsTheme.textStyles.displayMedium,
            color = HabitsTheme.colors.text,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp),
        )

        if (!state.isLoading && state.entries.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 32.dp),
                ) {
                    Text(
                        text = stringResource(Res.string.journal_empty_title),
                        style = HabitsTheme.textStyles.displayMedium,
                        color = HabitsTheme.colors.text,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = stringResource(Res.string.journal_empty_body),
                        style = HabitsTheme.textStyles.body,
                        color = HabitsTheme.colors.textFaint,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(state.entries) { entry ->
                    JournalItem(entry = entry)
                }
            }
        }
    }
}