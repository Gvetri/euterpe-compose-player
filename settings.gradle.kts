pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }

}

plugins {
    id("de.fayard.refreshVersions") version "0.10.1"
}
rootProject.name = "Euterpe"

include(":app")
include(":model")
include(":datasource:open")
include(":datasource:di")
include(":datasource:remote")
include(":datasource:cache")
include(":datasource:cache:cachemodel")
//include(":datasource:peertopeer")
include(":repository:open")
include(":repository:implementation")
include(":repository:di")
