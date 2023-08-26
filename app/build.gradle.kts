plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.example.budgetapp"
    compileSdk = 34
    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.budgetapp"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.0")

    implementation("com.github.zladnrms:RoundableLayout:1.1.3")

    implementation(platform("com.google.firebase:firebase-bom:32.2.2"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
        // Import the BoM for the Firebase platform
        implementation(platform("com.google.firebase:firebase-bom:32.2.2"))

        // Add the dependency for the Cloud Storage library
        // When using the BoM, you don't specify versions in Firebase library dependencies
        implementation("com.google.firebase:firebase-storage-ktx")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("com.github.bumptech.glide:compiler:4.12.0")

    }
