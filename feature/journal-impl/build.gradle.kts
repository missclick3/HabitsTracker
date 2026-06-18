plugins {
    id("habitstracker.android.library")
    id("habitstracker.compose.multiplatform")
    id("habitstracker.feature")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.journalApi)
            implementation(projects.database)
        }
    }
}

android {
    namespace = "com.missclick.habitstracker.journal.impl"
}