plugins {
    id("habitstracker.android.library")
    id("habitstracker.compose.multiplatform")
    id("habitstracker.feature")
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.homeApi)
            implementation(projects.database)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.client.java)
        }
    }
}

android {
    namespace = "com.missclick.habitstracker.home.impl"
}
