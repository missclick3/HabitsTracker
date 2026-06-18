import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.util.Properties

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

val localProps = Properties().apply {
    rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}

android {
    namespace = "com.missclick.habitstracker"

    defaultConfig {
        applicationId = "com.missclick.habitstracker"
        buildConfigField("String", "QUOTE_API_KEY", "\"${localProps["QUOTE_API_KEY"] ?: ""}\"")
    }

    buildFeatures {
        buildConfig = true
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
