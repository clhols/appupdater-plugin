plugins {
    kotlin("jvm") version "1.3.60"
    `maven-publish`
    `java-gradle-plugin`
}

dependencies {
    compileOnly(gradleApi())
    implementation("com.android.tools.build:gradle:3.5.2")
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
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register("appupdaterPlugin", MavenPublication::class.java) {
            groupId = "dk.youtec"
            artifactId = "appupdater-plugin"
            version = "1.0.1"

            artifact("$buildDir/libs/appupdater-plugin.jar")
        }
    }
}