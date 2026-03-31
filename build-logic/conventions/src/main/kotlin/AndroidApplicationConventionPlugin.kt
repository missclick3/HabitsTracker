import com.android.build.gradle.AppExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        val libs = getLibs()
        plugins.apply(libs.findPlugin("androidApplication").get().get().pluginId)

        extensions.configure<AppExtension> {
            compileSdkVersion(libs.findVersion("android-compileSdk").get().requiredVersion.toInt())

            defaultConfig.apply {
                minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt()
                targetSdk = libs.findVersion("android-targetSdk").get().requiredVersion.toInt()
                versionCode = 1
                versionName = "1.0"
            }

            packagingOptions.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"

            buildTypes {
                getByName("release") {
                    isMinifyEnabled = false
                }
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
        }
    }
}
