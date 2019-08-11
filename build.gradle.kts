plugins {
    kotlin("jvm") version "1.3.41"
    `maven-publish`
    `java-gradle-plugin`
}

dependencies {
    compileOnly(gradleApi())
    implementation("com.android.tools.build:gradle:3.4.2")
}

repositories {
    google()
    mavenCentral()
}

publishing {
    publications {
        create("appupdaterPlugin", MavenPublication::class.java) {
            groupId = "dk.youtec"
            artifactId = "appupdater"
            version = "1.0"

            from(components["java"])
        }
    }
}