import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("habitstracker.android.application")
    id("habitstracker.compose.multiplatform")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.database)
            implementation(projects.homeApi)
            implementation(projects.homeImpl)
            implementation(projects.journalApi)
            implementation(projects.journalImpl)
            implementation(libs.androidx.navigation3.runtime)
            implementation(libs.koin.compose)
            implementation(compose.materialIconsExtended)
        }

        androidMain.dependencies {
            implementation(libs.androidx.navigation3.ui)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

android {
    namespace = "com.missclick.habitstracker"

    defaultConfig {
        applicationId = "com.missclick.habitstracker"
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.missclick.habitstracker.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.missclick.habitstracker"
            packageVersion = "1.0.0"
        }
    }
}
