plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-android")
}

android {
    compileSdk = 30
    buildToolsVersion = "30.0.3"


    defaultConfig {
        minSdk = 23
        targetSdk = 30

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

}

dependencies {

    implementation(kotlin("stdlib-jdk7"))
    implementation(project(":model"))
    implementation(KotlinX.coroutines.core)

    //Dagger
    implementation(Google.dagger.hilt.android)
    kapt(Google.dagger.hilt.compiler)

    //Cache
    implementation(AndroidX.room.runtime)
    implementation(AndroidX.room.ktx)
    kapt(AndroidX.room.compiler)

}
