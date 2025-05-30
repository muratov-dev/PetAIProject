plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
}

android {
    namespace = "me.yeahapps.mypetai"
    compileSdk = 35

    defaultConfig {
        applicationId = "me.yeahapps.mypetai"
        minSdk = 26
        targetSdk = 35
        val majorVersion = 1
        val minorVersion = 0
        val patchVersion = 2

        versionCode = majorVersion * 10000 + minorVersion * 100 + patchVersion
        versionName = "${majorVersion}.${minorVersion}.${patchVersion}"
        base.archivesName = "PetAI-$versionName"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
        }
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.ui)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.timber)

    ksp(libs.hilt.compiler)
    implementation(libs.browser)
    implementation(libs.hilt.android)
    implementation(libs.hilt.compose)
    implementation(libs.bundles.okhttp)
    implementation(libs.bundles.retrofit)
    implementation(libs.haze)
    implementation(libs.coil)

    ksp(libs.room.compiler)
    implementation(libs.bundles.room)

    implementation(libs.bundles.media3)
    implementation(libs.media)

    implementation("com.google.android.play:review:2.0.2")

    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}