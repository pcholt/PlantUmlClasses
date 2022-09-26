plugins {
    id("java")
    kotlin("jvm")
}

group = "com.pcholt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("com.google.devtools.ksp:symbol-processing-api:1.7.10-1.0.6")

}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}