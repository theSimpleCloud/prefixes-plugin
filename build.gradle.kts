import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.8.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    java
}

allprojects {
    group = "app.simplecloud.plugin"
    version = "1.0-SNAPSHOT"

    apply {
        plugin("java")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("com.github.johnrengelman.shadow")
    }

    repositories {
        mavenCentral()
    }

    kotlin {
        jvmToolchain(17)
    }
}

subprojects {
    dependencies {
        implementation(kotlin("stdlib"))
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    tasks.named("shadowJar", ShadowJar::class) {
        mergeServiceFiles()
    }
}