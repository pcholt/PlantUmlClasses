plugins {
    id("java")
    kotlin("jvm")
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"
}

group = "com.pcholt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

//pluginManagement {
//    repositories {
//    }
//}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":processor"))
    ksp(project(":processor"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}