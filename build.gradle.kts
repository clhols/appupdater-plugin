plugins {
    kotlin("jvm") version "1.3.71"
    `maven-publish`
    `java-gradle-plugin`
}

dependencies {
    compileOnly(gradleApi())
    implementation("com.android.tools.build:gradle:3.6.0")
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
            version = "1.0.3"

            artifact("$buildDir/libs/appupdater-plugin.jar")
        }
    }
}