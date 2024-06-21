plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.google.services)
}

android {
    namespace = "exemple.udemy.java.olx"
    compileSdk = 34

    defaultConfig {
        applicationId = "exemple.udemy.java.olx"
        minSdk = 24
        targetSdk = 34
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.constraintlayout)
    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.realtime.database)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.auth)
    //https://github.com/santalu/maskara
    implementation(libs.maskara)
    //https://github.com/BlacKCaT27/CurrencyEditText
    implementation(libs.library)
    //Glide
    implementation(libs.bumptech.glide)

}