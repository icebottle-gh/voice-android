plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0"
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
    id("kotlin-kapt") //
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.temp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.temp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("net.folivo:trixnity-client:4.13.2")
    implementation("net.folivo:trixnity-client-media-okio:4.13.2")
    implementation("net.folivo:trixnity-client-repository-room:4.13.2")
    implementation("io.ktor:ktor-client-okhttp:3.1.1")

    //shimmer effect
    implementation ("com.google.accompanist:accompanist-placeholder-material3:0.33.2-alpha")

    implementation("androidx.room:room-runtime-android:2.7.0-rc01")//
    implementation("androidx.room:room-ktx:2.7.0-rc01")
//    implementation("androidx.compose.material3:material3-android:1.3.1")//
    ksp("androidx.room:room-compiler:2.7.0-rc01")//

    implementation("androidx.navigation:navigation-compose:2.8.5")//
    implementation("androidx.compose.ui:ui-android:1.7.6")
//    implementation("androidx.compose.material:material-android:1.7.6")//
    implementation("androidx.compose.ui:ui-tooling-preview-android:1.7.6")//


    implementation("io.coil-kt:coil-compose:2.4.0")//Image loading

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.10.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}