import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.compose.compose

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    id("org.jetbrains.compose") version "1.2.2"
    id("com.squareup.sqldelight") version "1.5.5"
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

object Deps {
    const val KotlinCoroutines = "1.6.4"
    const val JUnitJupiterVersion = "5.9.2"
    const val JUnitPlatformSuiteVersion = "1.9.2"
    const val CucumberVersion = "7.11.1"
    const val KtorVersion = "2.2.3"
}

dependencies {
    implementation(compose.desktop.macos_arm64)
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("com.squareup.sqldelight:sqlite-driver:1.5.4")
    implementation("dev.amaro:sonic:0.4.1")
    implementation("io.insert-koin:koin-core:3.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Deps.KotlinCoroutines}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:${Deps.KotlinCoroutines}")
    implementation("io.ktor:ktor-server-core:${Deps.KtorVersion}")
    implementation("io.ktor:ktor-server-netty:${Deps.KtorVersion}")
    implementation("io.ktor:ktor-server-websockets:${Deps.KtorVersion}")

    testImplementation(kotlin("test"))
    testImplementation(compose("org.jetbrains.compose.ui:ui-test-junit4"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Deps.KotlinCoroutines}")
    testImplementation("org.jetbrains.compose.ui:ui-test-desktop:1.1.1")

    testImplementation("org.junit.jupiter:junit-jupiter:${Deps.JUnitJupiterVersion}")
    testImplementation("org.junit.platform:junit-platform-suite:${Deps.JUnitPlatformSuiteVersion}")
    testImplementation("io.cucumber:cucumber-java:${Deps.CucumberVersion}")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:${Deps.CucumberVersion}")

    testImplementation("io.mockk:mockk:1.12.5")
    testImplementation("com.appmattus.fixture:fixture:1.2.0")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
    testImplementation("io.ktor:ktor-client-core:${Deps.KtorVersion}")
    testImplementation("io.ktor:ktor-client-cio:${Deps.KtorVersion}")
    testImplementation("io.ktor:ktor-client-websockets:${Deps.KtorVersion}")
}

tasks {
    jar {
        val classpath = configurations.runtimeClasspath
        // Input declaration is needed for the proper up-to-date checks
        inputs.files(classpath).withNormalizer(ClasspathNormalizer::class.java)
        manifest {
            attributes(
                "Class-Path" to classpath.map { cp -> cp.joinToString(" ") { it.absolutePath } }
            )
            attributes(
                "Implementation-Title" to project.name,
                "Implementation-Version" to archiveVersion,
                "Main-Class" to mainClassPath
            )
        }
    }

    test {
        useJUnitPlatform()
        systemProperty("cucumber.junit-platform.naming-strategy", "long")
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}
