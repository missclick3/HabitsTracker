package com.missclick.habitstracker.home.impl.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.missclick.habitstracker.core.design.HabitsTheme
import habitstracker.home_impl.generated.resources.Res
import habitstracker.home_impl.generated.resources.home_header_overline
import habitstracker.home_impl.generated.resources.home_header_title
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun HeaderBlock(greetingLabel: String, date: String) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .padding(
            horizontal = 24.dp,
            vertical = 16.dp,
        ),
    verticalArrangement = Arrangement.SpaceBetween,
) {
    //TODO add avatar picture, when account feature is ready
    GreetingsBlock(greetingLabel, modifier = Modifier.fillMaxWidth())
    Spacer(modifier = Modifier.size(24.dp))
    Text(
        text = date,
        style = HabitsTheme.textStyles.headerDate,
        color = HabitsTheme.colors.textPrimary,
    )
}

@Composable
private fun GreetingsBlock(greetingLabel: String, modifier: Modifier = Modifier) = Row(
    modifier = modifier
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(color = HabitsTheme.colors.success, shape = RoundedCornerShape(corner = CornerSize(16.dp)))
    )
    Spacer(modifier = Modifier.size(16.dp))
    Column {
        Text(
            text = (stringResource(Res.string.home_header_overline)).uppercase(),
            style = HabitsTheme.textStyles.headerOverline,
            color = HabitsTheme.colors.textTertiary,
        )
        Text(
            text = greetingLabel,
            style = HabitsTheme.textStyles.headerName,
            color = HabitsTheme.colors.textPrimary,
        )
    }
}