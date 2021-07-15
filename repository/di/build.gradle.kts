plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")

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

    hilt {
        enableExperimentalClasspathAggregation = true
    }

}

dependencies {
    implementation(kotlin("stdlib-jdk7"))
    val arrowMeta = "io.arrow-kt:arrow-meta:_"
    val arrowOptics = "io.arrow-kt:arrow-optics:_"

    implementation(kotlin("stdlib-jdk7"))
    implementation(project(":model"))
    implementation(project(":datasource:open"))
    implementation(project(":datasource:di"))
    implementation(project(":repository:open"))
    implementation(project(":repository:implementation"))
    implementation(KotlinX.coroutines.core)

    //Dagger
    implementation(Google.dagger.hilt.android)
    kapt(Google.dagger.hilt.compiler)
    //Cache
    implementation(AndroidX.room.runtime)
    implementation(AndroidX.room.ktx)
    kapt(AndroidX.room.compiler)

    //Arrow
    implementation(arrowOptics)
    kapt(arrowMeta)
}
