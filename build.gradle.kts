plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "2.0.1"
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.beryx.jlink") version "4.0.1"
}

group = "org.notiva"
version = "1.0"

repositories {
    mavenCentral()
}

val junitVersion = "5.11.4"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("org.notiva.beatrush")
    mainClass.set("org.notiva.beatrush.App")
}

javafx {
    version = "25"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.media")
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(
        listOf(
            "--strip-debug", "--compress", "zip-6",
            "--no-header-files", "--no-man-pages"
        )
    )
    launcher {
        name = "app"
    }
    jpackage {
        icon = "src/main/resources/image/material/beatrush-icon.ico"
        imageName = "BeatRush"
        installerType = "app-image"
        skipInstaller = true
    }
}