plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.compose.compiler.gradlePlugin)
    compileOnly(libs.compose.hotReload.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "habitstracker.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidLibrary") {
            id = "habitstracker.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("composeMultiplatform") {
            id = "habitstracker.compose.multiplatform"
            implementationClass = "ComposeMultiplatformConventionPlugin"
        }

        register("feature") {
            id = "habitstracker.feature"
            implementationClass = "FeatureConventionPlugin"
        }
    }
}
