plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.21"
}


android {
    namespace = "com.example.checkit"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.checkit"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.navigation.compose)
    implementation(libs.ads.mobile.sdk)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Retrofit (HTTP client)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Converter for JSON serialization (using kotlinx.serialization)
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    // OkHttp (HTTP client implementation for Retrofit)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // OkHttp logging interceptor (very helpful for debugging requests/responses)
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // KotlinX Serialization runtime (for defining data classes)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")


    implementation("androidx.security:security-crypto:1.1.0")
    //Hilt
    kapt("com.google.dagger:hilt-compiler:2.57.2")
    implementation(libs.androidx.hilt.navigation.compose)
    implementation("com.google.dagger:hilt-android:2.57.2")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")

    implementation("androidx.compose.material:material-icons-extended")

    //to visualize images as photo or URLs
    implementation("io.coil-kt:coil-compose:2.5.0")

    // For image management (Coil)
    implementation("io.coil-kt:coil-compose:2.5.0")

    // For icons such as PhotoCamera
    implementation("androidx.compose.material:material-icons-extended")

    //For scanning QR (ZXing)
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    // Per gestire i risultati delle Activity in Compose (rememberLauncherForActivityResult)
    implementation("androidx.activity:activity-compose:1.8.2")

}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
