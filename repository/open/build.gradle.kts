plugins {
    id("java-library")
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    val arrowCore = "io.arrow-kt:arrow-core:_"
    val arrowSyntax = "io.arrow-kt:arrow-syntax:_"
    implementation(kotlin("stdlib-jdk7"))
    implementation(project(":model"))
    implementation(arrowCore)
    implementation(KotlinX.coroutines.core)
    kapt(arrowSyntax)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
