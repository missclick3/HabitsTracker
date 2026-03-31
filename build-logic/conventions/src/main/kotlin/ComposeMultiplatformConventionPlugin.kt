import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        val libs = getLibs()
        plugins.apply(libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
        plugins.apply(libs.findPlugin("composeMultiplatform").get().get().pluginId)
        plugins.apply(libs.findPlugin("composeCompiler").get().get().pluginId)
        plugins.apply(libs.findPlugin("composeHotReload").get().get().pluginId)

        extensions.configure<KotlinMultiplatformExtension> {
            androidTarget {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_11)
                }
            }

            listOf(
                iosArm64(),
                iosSimulatorArm64()
            ).forEach { iosTarget ->
                iosTarget.binaries.framework {
                    baseName = moduleName
                    isStatic = true
                }
            }

            jvm()

            sourceSets.configureEach {
                when (name) {
                    "androidMain" -> dependencies {
                        implementation(libs.findLibrary("compose.uiToolingPreview").get().get())
                        implementation(libs.findLibrary("androidx.activity.compose").get().get())
                    }

                    "commonMain" -> dependencies {
                        implementation(libs.findLibrary("compose.runtime").get().get())
                        implementation(libs.findLibrary("compose.foundation").get().get())
                        implementation(libs.findLibrary("compose.material3").get().get())
                        implementation(libs.findLibrary("compose.ui").get().get())
                        implementation(libs.findLibrary("compose.components.resources").get().get())
                        implementation(libs.findLibrary("compose.uiToolingPreview").get().get())
                        implementation(libs.findLibrary("androidx.lifecycle.viewmodelCompose").get().get())
                        implementation(libs.findLibrary("androidx.lifecycle.runtimeCompose").get().get())
                    }

                    "commonTest" -> dependencies {
                        implementation(libs.findLibrary("kotlin.test").get().get())
                    }
                }
            }
        }
    }
}
