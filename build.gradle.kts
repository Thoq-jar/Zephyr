plugins {
  id("java")
  id("application")
  id("org.jetbrains.kotlin.jvm") version "1.9.23"
  id("org.javamodularity.moduleplugin") version "1.8.12"
  id("org.openjfx.javafxplugin") version "0.0.13"
  id("org.beryx.jlink") version "2.25.0"
}

group = "dev.thoq"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val junitVersion by extra("5.10.2")
val gsonVersion by extra("2.10.1")
val javaVersion = 17
val javaFxVersion = "$javaVersion.0.6"

tasks.withType<JavaCompile> {
  options.encoding = "UTF-8"
}

application {
  mainModule.set("dev.thoq.zephyr")
  mainClass.set("dev.thoq.zephyr.ZephyrApplication")
}

kotlin {
  jvmToolchain(javaVersion)
}

javafx {
  version = javaFxVersion
  modules = listOf("javafx.controls", "javafx.fxml", "javafx.media")
}

dependencies {
  implementation("com.google.code.gson:gson:$gsonVersion")
  testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks.test {
  useJUnitPlatform()
}

jlink {
  val zipPath = layout.buildDirectory.file("distributions/app-${javafx.platform.classifier}.zip")
  imageZip.set(zipPath)
  options.addAll(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
}

tasks.jlinkZip {
  group = "distribution"
}