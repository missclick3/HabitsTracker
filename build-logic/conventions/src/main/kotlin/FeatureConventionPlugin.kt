import org.gradle.api.Plugin
import org.gradle.api.Project

class FeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        val libs = getLibs()

        pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
            dependencies.add(
                "commonMainImplementation",
                dependencies.project(mapOf("path" to ":core"))
            )
            dependencies.add(
                "commonMainImplementation",
                libs.findLibrary("koin.core").get().get()
            )
            dependencies.add(
                "commonMainImplementation",
                libs.findLibrary("kotlinx.coroutines.core").get().get()
            )
            dependencies.add(
                "commonMainImplementation",
                libs.findLibrary("kotlinx.datetime").get().get()
            )
        }
    }
}
