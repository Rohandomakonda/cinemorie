plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize") // This line should work now
}


android {
    namespace = "com.example.netflix"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.netflix"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Retrofit & Gson
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

// Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
//videoplayer
    implementation ("com.google.android.exoplayer:exoplayer:2.19.1")  // Or the latest version

    implementation( "androidx.compose.material:material-icons-extended:1.4.3")

    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

        implementation ("androidx.mediarouter:mediarouter:1.6.0")
        implementation ("com.google.android.gms:play-services-cast-framework:21.3.0")





    implementation ("com.google.android.exoplayer:extension-cast:2.19.1")

//
    implementation("org.nanohttpd:nanohttpd:2.3.1")

// ViewModel + LiveData (for MVM / Jetpack Compose)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    implementation("com.google.android.gms:play-services-auth:20.7.0")

// Optional: OkHttp logging (for network debugging)
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")
    implementation ("com.google.android.material:material:1.9.0")

    implementation("androidx.compose.material3:material3:1.1.0")
    implementation("org.apache.commons:commons-text:1.10.0")
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}