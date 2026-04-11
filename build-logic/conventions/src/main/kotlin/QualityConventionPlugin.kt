import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jlleitschuh.gradle.ktlint.KtlintExtension

class QualityConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        val libs = getLibs()
        val detektPluginId = libs.findPlugin("detekt").get().get().pluginId
        val ktlintPluginId = libs.findPlugin("ktlint").get().get().pluginId

        applyLintPluginsForKotlin(detektPluginId, ktlintPluginId)
        configureDetekt(detektPluginId)
        configureKtlint(ktlintPluginId)
        configureAndroidLint()
    }
}

private fun Project.applyLintPluginsForKotlin(detektPluginId: String, ktlintPluginId: String) {
    fun applyLintPlugins() {
        pluginManager.apply(detektPluginId)
        pluginManager.apply(ktlintPluginId)
    }

    pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") { applyLintPlugins() }
    pluginManager.withPlugin("org.jetbrains.kotlin.jvm") { applyLintPlugins() }
    pluginManager.withPlugin("org.jetbrains.kotlin.android") { applyLintPlugins() }
}

private fun Project.configureDetekt(detektPluginId: String) {
    pluginManager.withPlugin(detektPluginId) {
        extensions.configure<DetektExtension> {
            buildUponDefaultConfig = true
            allRules = false
            parallel = true
            ignoreFailures = false
            config.setFrom(rootProject.file("detekt.yml"))
        }

        tasks.withType<Detekt>().configureEach {
            include("**/*.kt", "**/*.kts")
            exclude("**/build/**")
            exclude("**/generated/**")

            reports {
                html.required.set(true)
                xml.required.set(true)
                sarif.required.set(true)
                md.required.set(false)
                txt.required.set(false)
            }
        }
    }
}

private fun Project.configureKtlint(ktlintPluginId: String) {
    pluginManager.withPlugin(ktlintPluginId) {
        extensions.configure<KtlintExtension> {
            android.set(true)
            ignoreFailures.set(false)
            outputToConsole.set(true)

            filter {
                include("**/*.kt")
                include("**/*.kts")
                exclude("**/build/**")
                exclude("**/generated/**")
                exclude { element ->
                    val normalizedPath = element.file.absolutePath.replace('\\', '/')
                    normalizedPath.contains("/build/") || normalizedPath.contains("/generated/")
                }
            }
        }
    }
}

private fun Project.configureAndroidLint() {
    val suppressedChecks = setOf(
        "AndroidGradlePluginVersion",
        "GradleDependency",
        "NewerVersionAvailable",
        "MonochromeLauncherIcon",
        "ObsoleteSdkInt"
    )

    pluginManager.withPlugin("com.android.application") {
        extensions.configure<ApplicationExtension> {
            lint {
                abortOnError = true
                warningsAsErrors = true
                checkDependencies = true
                checkReleaseBuilds = true
                htmlReport = true
                xmlReport = true
                textReport = true
                disable += suppressedChecks
            }
        }
    }

    pluginManager.withPlugin("com.android.library") {
        extensions.configure<LibraryExtension> {
            lint {
                abortOnError = true
                warningsAsErrors = true
                checkDependencies = true
                checkReleaseBuilds = true
                htmlReport = true
                xmlReport = true
                textReport = true
                disable += suppressedChecks
            }
        }
    }
}
