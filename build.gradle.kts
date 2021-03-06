plugins {
    id("io.gitlab.arturbosch.detekt") apply false
    id("org.jmailen.kotlinter") apply false
}

@Suppress("GradlePluginVersion") buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:_")
        classpath("com.android.tools.build:gradle:_")
        classpath(Google.dagger.hilt.android.gradlePlugin)
    }
}

allprojects {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
