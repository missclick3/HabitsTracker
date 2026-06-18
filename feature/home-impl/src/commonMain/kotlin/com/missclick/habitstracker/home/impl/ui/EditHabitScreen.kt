package com.missclick.habitstracker.home.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.missclick.habitstracker.core.design.HabitsTheme
import com.missclick.habitstracker.core.model.HabitKind
import com.missclick.habitstracker.home.impl.presenter.editHabit.EditHabitIntent
import com.missclick.habitstracker.home.impl.presenter.editHabit.EditHabitState
import habitstracker.home_impl.generated.resources.Res
import habitstracker.home_impl.generated.resources.home_create_save
import habitstracker.home_impl.generated.resources.home_create_title
import habitstracker.home_impl.generated.resources.home_edit_delete
import habitstracker.home_impl.generated.resources.home_edit_habit_name_label
import habitstracker.home_impl.generated.resources.home_edit_kind_binary
import habitstracker.home_impl.generated.resources.home_edit_kind_count
import habitstracker.home_impl.generated.resources.home_edit_kind_label
import habitstracker.home_impl.generated.resources.home_edit_save
import habitstracker.home_impl.generated.resources.home_edit_target_label
import habitstracker.home_impl.generated.resources.home_edit_title
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun EditHabitScreen(
    state: EditHabitState,
    onIntent: (EditHabitIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HabitsTheme.colors.background)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = stringResource(if (state.isCreateMode) Res.string.home_create_title else Res.string.home_edit_title),
            style = HabitsTheme.textStyles.displayMedium,
            color = HabitsTheme.colors.text,
        )

        OutlinedTextField(
            value = state.title,
            onValueChange = { onIntent(EditHabitIntent.TitleChanged(it)) },
            label = { Text(stringResource(Res.string.home_edit_habit_name_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(Res.string.home_edit_kind_label),
                style = HabitsTheme.textStyles.body,
                color = HabitsTheme.colors.textFaint,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = state.kind == HabitKind.Binary,
                    onClick = { onIntent(EditHabitIntent.KindChanged(HabitKind.Binary)) },
                    label = { Text(stringResource(Res.string.home_edit_kind_binary)) },
                )
                FilterChip(
                    selected = state.kind == HabitKind.Count,
                    onClick = { onIntent(EditHabitIntent.KindChanged(HabitKind.Count)) },
                    label = { Text(stringResource(Res.string.home_edit_kind_count)) },
                )
            }
        }

        if (state.kind == HabitKind.Count) {
            OutlinedTextField(
                value = state.targetCountInput,
                onValueChange = { onIntent(EditHabitIntent.TargetCountChanged(it)) },
                label = { Text(stringResource(Res.string.home_edit_target_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onIntent(EditHabitIntent.SaveClicked) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isSaving && state.title.isNotBlank(),
        ) {
            Text(stringResource(if (state.isCreateMode) Res.string.home_create_save else Res.string.home_edit_save))
        }

        if (!state.isCreateMode) {
            OutlinedButton(
                onClick = { onIntent(EditHabitIntent.DeleteClicked) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSaving,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = HabitsTheme.colors.accent,
                ),
            ) {
                Text(stringResource(Res.string.home_edit_delete))
            }
        }
    }
}