plugins {
    kotlin("jvm") version "1.3.50"
    `maven-publish`
    `java-gradle-plugin`
}

dependencies {
    compileOnly(gradleApi())
    implementation("com.android.tools.build:gradle:3.5.0")
}

repositories {
    google()
    mavenCentral()
    jcenter()
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