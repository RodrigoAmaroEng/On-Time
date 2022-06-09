import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    id("org.jetbrains.compose") version "1.1.0"
    id("com.squareup.sqldelight") version "1.5.3"
}

group = "dev.amaro"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

sqldelight {
    database("MyTasks") { // This will be the name of the generated database class.
        packageName = "dev.amaro.on_time.log"
    }
}


dependencies {
    implementation(compose.desktop.currentOs)
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    implementation("com.squareup.sqldelight:sqlite-driver:1.5.3")
    implementation("dev.amaro:sonic:0.3.1")
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.12.4")
    testImplementation("com.appmattus.fixture:fixture:1.2.0")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}
