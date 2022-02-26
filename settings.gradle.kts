pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }

}

plugins {
    id("de.fayard.refreshVersions") version "0.40.1"
}
rootProject.name = "Euterpe"

include(":app")
include(":model")
include(":datasource:open")
include(":datasource:di")
include(":datasource:remote")
include(":datasource:cache")
include(":datasource:cache:cachemodel")
include(":repository:open")
include(":repository:implementation")
include(":repository:di")
