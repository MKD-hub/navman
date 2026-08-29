plugins {
  kotlin("jvm") version "2.0.0"
}

group = "com.rss.navmanager"
version = "1.0.0"

dependencies {
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
  testImplementation(kotlin("test"))
}

tasks.test {
  useJUnitPlatform()
}

tasks.withType<Test> {
    testLogging {
        events("failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showStandardStreams = true
    }
}

