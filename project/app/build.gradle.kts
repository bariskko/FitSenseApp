plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.ceng.madlab.FitSenseApp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.ceng.madlab.FitSenseApp"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Test kütüphaneleri
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // --- FIREBASE BÖLÜMÜ (DÜZELTİLDİ) ---
    // 1. Firebase BoM (Versiyon Yönetici)
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))

    // 2. Firebase Kütüphaneleri (Versiyon numarası olmadan)
    // Not: "libs.firebase.auth" satırını kaldırdık, çünkü o eski bir versiyonu zorluyordu.
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
}