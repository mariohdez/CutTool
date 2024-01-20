plugins {
    java
    application
}

group = "org.mariocoding"
version = "1.0-SNAPSHOT"
val junitVersion = "5.9.2"
val javaLanguageVersion = 17

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${junitVersion}")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaLanguageVersion))
    }
}

application {
    mainClass.set("org.mariocoding.Cut")
}

tasks.jar {
    manifest.attributes["Main-Class"] = application.mainClass
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}