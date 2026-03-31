plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.android.lint) apply false
}

val checkNoHardcodedColors by tasks.registering {
    group = "verification"
    description = "Fails if hardcoded Color(0x...) is used outside core.design."
    notCompatibleWithConfigurationCache("Performs direct file-content scanning across project source roots.")

    doLast {
        val hardcodedColorPattern = Regex("""Color\(0x[0-9A-Fa-f]{8}\)""")
        val violatingFiles = fileTree(rootDir) {
            include("**/src/**/*.kt")
            exclude("**/build/**")
            exclude("core/src/commonMain/kotlin/com/missclick/habitstracker/core/design/**")
        }.files.filter { file ->
            hardcodedColorPattern.containsMatchIn(file.readText())
        }

        check(violatingFiles.isEmpty()) {
            buildString {
                appendLine("Hardcoded colors found outside core.design:")
                violatingFiles.forEach { appendLine("- ${it.relativeTo(rootDir).path}") }
            }
        }
    }
}

subprojects {
    tasks.matching { it.name == "check" }.configureEach {
        dependsOn(rootProject.tasks.named("checkNoHardcodedColors"))
    }
}
