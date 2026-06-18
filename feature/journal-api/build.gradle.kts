plugins {
    id("habitstracker.android.library")
    id("habitstracker.compose.multiplatform")
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core)
            implementation(libs.kotlinx.serialization.core)
        }
    }
}

android {
    namespace = "com.missclick.habitstracker.journal.api"
}