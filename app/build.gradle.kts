plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.vehicletracking"
    compileSdk = 35
    android {
        buildFeatures {
            buildConfig = true
        }
    }
    defaultConfig {
        applicationId = "com.example.vehicletracking"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "GOOGLE_MAPS_API_KEY",
            "\"AIzaSyCVS-NTjrfxc-l5iRXFADlP1N81fAypV2U\""
        )
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(platform(libs.firebase.bom))
    implementation(libs.play.services.auth)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.kotlin.stdlib.jdk7)
    implementation(libs.material.v190)
    implementation(libs.sdp.android )
    implementation(libs.kprogresshud)
    implementation(libs.play.services.maps.v1920)
    implementation(libs.play.services.location.v1700)

    implementation ("com.google.android.gms:play-services-auth:20.1.0")
}
