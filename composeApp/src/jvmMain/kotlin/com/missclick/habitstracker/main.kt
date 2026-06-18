package com.missclick.habitstracker

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import habitstracker.composeapp.generated.resources.Res
import habitstracker.composeapp.generated.resources.app_name
import java.io.File
import java.util.Properties
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun main() =
    application {
        val localProps = Properties().apply {
            File("local.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
        }
        val platformModule = module {
            single(named("quoteApiKey")) { localProps.getProperty("QUOTE_API_KEY", "") }
        }
        Window(
            onCloseRequest = ::exitApplication,
            title = runBlocking { getString(Res.string.app_name) },
        ) {
            App(platformModule = platformModule)
        }
    }
