package com.missclick.habitstracker

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun MainViewController(quoteApiKey: String = "") =
    ComposeUIViewController {
        App(
            platformModule = module {
                single(named("quoteApiKey")) { quoteApiKey }
            },
        )
    }
