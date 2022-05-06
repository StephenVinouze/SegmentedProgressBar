plugins {
    id("org.jetbrains.compose") version "1.1.1"
    id("com.android.application")
    kotlin("android")
}

dependencies {
    implementation(project(":common"))
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("com.google.android.material:material:1.6.0")
    implementation("com.airbnb.android:lottie-compose:4.2.2")
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "com.stephenvinouze.segmentedprogressbar"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}