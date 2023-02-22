import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.22"
    `java-library`
    `maven-publish`
}

group = "com.hoseus"
version = "0.1.0"

val kotestVersion by extra("5.5.4")

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    testImplementation("io.kotest:kotest-runner-junit5:${kotestVersion}")
    testImplementation("io.kotest:kotest-assertions-core:${kotestVersion}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("snapshot") {
            artifactId = "${project.name}"
            version = "${version}-SNAPSHOT"

            from(components["java"])
        }

        create<MavenPublication>("release") {
            artifactId = "${project.name}"
            version = "${version}"
            from(components["java"])
        }
    }

    repositories {
        mavenLocal()
        mavenCentral()
    }

    /**
     * Only publish if publication and repository have the same name.
     * This way snapshots are only published to snapshot repository,
     * and releases to release repository.
     **/
    afterEvaluate {
        tasks.withType<PublishToMavenRepository> {
            onlyIf {
                publication.name == repository.name
            }
        }
    }
}
