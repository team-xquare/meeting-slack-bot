plugins {
    kotlin("jvm") version "1.5.10"
}

group = "io.github.team-xquare"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.slack.api:bolt-socket-mode:1.8.1")
    implementation("javax.websocket:javax.websocket-api:1.1")
    implementation("org.glassfish.tyrus.bundles:tyrus-standalone-client:1.17")
    implementation(kotlin("stdlib"))
}
