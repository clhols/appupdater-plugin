plugins {
    kotlin("jvm") version "1.4.10"
    `maven-publish`
    `java-gradle-plugin`
}

dependencies {
    compileOnly(gradleApi())
    implementation("com.android.tools.build:gradle:4.1+")
}

repositories {
    google()
    mavenCentral()
    jcenter()
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/clhols/appupdater-plugin")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("PASSWORD")
            }
        }
    }
    publications {
        register("appupdaterPlugin", MavenPublication::class.java) {
            groupId = "dk.youtec"
            artifactId = "appupdater-plugin"
            version = "1.0.6-SNAPSHOT"

            artifact("$buildDir/libs/appupdater-plugin.jar")
        }
    }
}