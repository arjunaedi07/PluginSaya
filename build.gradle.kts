buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.1.2")
        classpath("com.github.recloudstream:gradle:cce1b8d84d")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
    }
}

plugins {
    id("com.android.library") apply false
    kotlin("android") apply false
    id("com.lagradost.cloudstream3.gradle") apply false
}


allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

subprojects {

    apply(plugin = "com.android.library")
    apply(plugin = "kotlin-android")
    apply(plugin = "com.lagradost.cloudstream3.gradle")

    cloudstream {
        setRepo(System.getenv("GITHUB_REPOSITORY")
            ?: "https://github.com/arjunaedi07/PluginSaya")

        authors = listOf("Arjuna")
    }

    android {
        compileSdkVersion(33)

        defaultConfig {
            minSdk = 21
            targetSdk = 33
        }
    }

    dependencies {
        "cloudstream"("com.lagradost:cloudstream3:pre-release")
    }
}
