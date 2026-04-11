package com.missclick.habitstracker

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import habitstracker.composeapp.generated.resources.Res
import habitstracker.composeapp.generated.resources.app_name
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString

fun main() =
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = runBlocking { getString(Res.string.app_name) },
        ) {
            App()
        }
    }
