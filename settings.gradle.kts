pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

include("chat-tab-api", "chat-tab-minestom", "chat-tab-paper", "chat-tab-shared", "chat-tab-spigot")

rootProject.name = "ChatTab-Plugin"