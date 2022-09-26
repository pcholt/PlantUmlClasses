import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10" apply false
//    application
}

buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.7.10"))
    }
}

group = "com.pcholt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
//    testImplementation(kotlin("test"))
}

//tasks.test {
//    useJUnitPlatform()
//}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

//application {
//    mainClass.set("MainKt")
//}