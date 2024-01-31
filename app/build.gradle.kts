plugins {
    kotlin("android") version "1.5.21"
    kotlin("android.extensions") version "1.1.0"
}

android {
    namespace = "com.example.front_end_android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.front_end_android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    viewBinding {
        enable=true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.square.retrofit2:retrofit:2.9.0")
    implementation ("com.square.retrofit2:converter-gson:2.9.0")
    implementation ("com.square.okhttp3:okhttp:4.8.0")
    implementation ("com.square.okhttp3:logging-interceptor:4.8.0")
}