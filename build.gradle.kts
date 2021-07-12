val slackSdkVersion = "1.8.1"

plugins {
    kotlin("jvm") version "1.5.10"
    id("application")
}

group = "io.github.team-xquare"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.litote.kmongo:kmongo:4.2.8")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.slack.api:bolt-socket-mode:$slackSdkVersion")
    implementation("com.slack.api:slack-api-client:$slackSdkVersion")
    implementation("javax.websocket:javax.websocket-api:1.1")
    implementation("org.glassfish.tyrus.bundles:tyrus-standalone-client:1.17")
    implementation("org.slf4j:slf4j-simple:1.7.31")
}

application {
    mainClass.set("AppKt")
}