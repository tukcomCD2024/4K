plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {

    packagingOptions {
        exclude( "META-INF/DEPENDENCIES")
        exclude( "META-INF/INDEX.LIST")
    }


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
    implementation("androidx.core:core-ktx:+")
    implementation("com.google.firebase:firebase-messaging:24.0.0")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation ("com.android.volley:volley:1.2.1")
    //https://mvnrepository.com/artifact/org.webrtc/google-webrtc
    //implementation("org.webrtc:google-webrtc:1.0.32006")

    implementation("com.mesibo.api:webrtc:1.0.5")
    implementation("org.java-websocket:Java-WebSocket:1.5.3")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("com.guolindev.permissionx:permissionx:1.6.1")

    implementation ("com.google.cloud:google-cloud-speech:1.29.1")
    implementation ("com.google.auth:google-auth-library-oauth2-http:0.26.0")
    implementation ("io.grpc:grpc-okhttp:1.38.1")
    implementation ("io.grpc:grpc-stub:1.38.1")
    implementation ("com.google.api:gax:1.58.0")

    implementation ("com.google.firebase:firebase-bom:31.3.0")
    implementation ("com.google.firebase:firebase-analytics-ktx")
    implementation ("com.google.firebase:firebase-messaging:23.0.3")

}