import com.android.build.gradle.internal.lint.AndroidLintTask
import de.fayard.refreshVersions.core.versionFor
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("io.gitlab.arturbosch.detekt")
    id("org.jmailen.kotlinter")
}

android {
    compileSdk = 31
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "com.codingpizza.euterpe"
        minSdk = 24
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    lint {
        lintConfig = rootProject.file("build-config/lint.xml")
        warningsAsErrors = true
        sarifReport = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true",
            "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=kotlin.RequiresOptIn"
        )
    }

    composeOptions {
        kotlinCompilerExtensionVersion = versionFor(AndroidX.compose.runtime)
    }

    hilt {
        enableAggregatingTask = true
    }

    packagingOptions {
        resources {
            excludes+= "META-INF/DEPENDENCIES" +
                "META-INF/NOTICE" +
                "META-INF/LICENSE" +
                "META-INF/LICENSE.txt" +
                "META-INF/NOTICE.txt" +
                "META-INF/AL2.0.txt" +
                "META-INF/AL2.0" +
                "META-INF/LGPL2.1"
        }
    }
}

detekt {
    input = files("src/main/java", "src/main/kotlin")
    config = rootProject.files("build-config/detekt.yml")
    buildUponDefaultConfig = true
    reports {
        sarif {
            enabled = true
        }
    }
}

kotlinter {
    disabledRules = arrayOf("no-wildcard-imports")
}

dependencies {
    val arrowOptics = "io.arrow-kt:arrow-optics:_"
    val arrowMeta = "io.arrow-kt:arrow-meta:_"
    val exoplayerCore = "com.google.android.exoplayer:exoplayer-core:_"
    val exoplayerDash = "com.google.android.exoplayer:exoplayer-dash:_"
    val exoplayerUiLibrary = "com.google.android.exoplayer:exoplayer-ui:_"
    val exoplayerHls = "com.google.android.exoplayer:exoplayer-hls:_"
    val exoplayerMediaSession = "com.google.android.exoplayer:extension-mediasession:_"
    val exoplayerMediaRouter = "com.google.android.exoplayer:extension-cast:_"
    val mediaRouter = "androidx.mediarouter:mediarouter:_"

    //Modules
    implementation(project(":model"))
    implementation(project(":repository:open"))
    implementation(project(":repository:di"))
    implementation(AndroidX.appCompat)
    implementation(AndroidX.core.ktx)
    implementation(AndroidX.lifecycle.liveDataKtx)
    implementation(AndroidX.lifecycle.viewModelKtx)
    implementation(KotlinX.coroutines.core)

    implementation(AndroidX.activity.compose)
    implementation(AndroidX.navigation.compose)
    implementation(AndroidX.compose.runtime)
    implementation(AndroidX.compose.foundation)
    implementation(AndroidX.compose.foundation.layout)
    implementation(AndroidX.compose.material)
    implementation(AndroidX.compose.runtime.liveData)
    implementation(AndroidX.compose.ui.tooling)
    implementation(AndroidX.compose.material.icons.extended)
    implementation(Google.android.material)

    implementation(exoplayerCore)
    implementation(exoplayerDash)
    implementation(exoplayerUiLibrary)
    implementation(exoplayerHls)
    implementation(exoplayerMediaSession)

    implementation(Google.dagger.hilt.android)
    kapt(Google.dagger.hilt.compiler)
    implementation(AndroidX.hilt.navigationCompose)

    // Chromecast
    implementation(exoplayerMediaRouter)
    implementation(mediaRouter)

    //Arrow
    implementation(arrowOptics)
    kapt(arrowMeta)

    //Utils
    implementation(JakeWharton.timber)

    testImplementation(Testing.junit4)
    testImplementation(AndroidX.test.ext.junit)
    testImplementation(AndroidX.test.espresso.core)
    androidTestImplementation(AndroidX.compose.ui.testJunit4)
    debugImplementation(AndroidX.compose.ui.testManifest)

}

tasks {
    withType<Detekt> {
        // Required for type resolution
        jvmTarget = "1.8"
    }

    val staticAnalysis by registering {
        val detektRelease by getting(Detekt::class)
        val androidLintRelease = named<AndroidLintTask>("lintRelease")

        dependsOn(detekt, detektRelease, androidLintRelease, lintKotlin)
    }

    register<Sync>("collectSarifReports") {
        val detektRelease by getting(Detekt::class)
        val androidLintRelease = named<AndroidLintTask>("lintRelease")

        mustRunAfter(detekt, detektRelease, androidLintRelease, lintKotlin, staticAnalysis)

        from(detektRelease.sarifReportFile) {
            rename { "detekt-release.sarif" }
        }
        from(detekt.get().sarifReportFile) {
            rename { "detekt.sarif" }
        }
        from(androidLintRelease.get().sarifReportOutputFile.get().asFile) {
            rename { "android-lint.sarif" }
        }

        into("$buildDir/reports/sarif")

        doLast {
            logger.info("Copied ${inputs.files.files.filter { it.exists() }} into ${outputs.files.files}")
            logger.info("Output dir contents:\n${outputs.files.files.first().listFiles()?.joinToString()}")
        }
    }
}
