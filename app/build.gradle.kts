plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.ksp)
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.shadow.deepseekimp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.shadow.deepseekimp"
        minSdk = 26
        targetSdk = 35
        versionCode = providers.gradleProperty("chatVersionCode").get().toInt()
        versionName = providers.gradleProperty("chatVersionName").get()
        setProperty("archivesBaseName", "${providers.gradleProperty("chatName").get()} ${versionName}")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL_DEEP", "\"https://api.deepseek.com/\"")
        buildConfigField("String", "BASE_URL_QWEN", "\"https://dashscope-intl.aliyuncs.com/\"")
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
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-opt-in=androidx.compose.animation.ExperimentalSharedTransitionApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.androidx.firebase.bom))
    implementation(platform(libs.androidx.compose.bom))
    ksp(libs.bundles.kspBundles)
    implementation(libs.bundles.hilt)
    implementation(libs.bundles.firebase)
    implementation(libs.bundles.storage)
    implementation(libs.bundles.network)
    implementation(libs.bundles.ui)
    implementation(libs.bundles.core)
    implementation(libs.bundles.markdown)
    implementation(libs.bundles.test)
}