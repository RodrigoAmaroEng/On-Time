import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.compose.compose

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    id("org.jetbrains.compose") version "1.1.1"
    id("com.squareup.sqldelight") version "1.5.3"
}

group = "dev.amaro"
version = "1.0-SNAPSHOT"
val mainClassName = "MainKt"
val mainClassPath = "$group.on_time.$mainClassName"


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
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    implementation("com.squareup.sqldelight:sqlite-driver:1.5.3")
    implementation("dev.amaro:sonic:0.4.1")
    implementation("io.insert-koin:koin-core:3.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.2")

    testImplementation(kotlin("test"))
    testImplementation(compose("org.jetbrains.compose.ui:ui-test-junit4"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.2")
    testImplementation("org.jetbrains.compose.ui:ui-test-desktop:1.1.1")
    testImplementation("org.jbehave:jbehave-core:5.0")
    testImplementation("io.insert-koin:koin-test:3.2.0")
    testImplementation("io.mockk:mockk:1.12.4")
    testImplementation("com.appmattus.fixture:fixture:1.2.0")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
}

tasks.jar {
    val classpath = configurations.runtimeClasspath
    // Input declaration is needed for the proper up-to-date checks
    inputs.files(classpath).withNormalizer(ClasspathNormalizer::class.java)
    manifest {
        attributes(
            "Class-Path" to classpath.map { cp -> cp.joinToString(" ") { it.absolutePath } }
        )
        attributes( "Implementation-Title" to project.name, "Implementation-Version" to version, "Main-Class" to mainClassPath)
    }
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}
