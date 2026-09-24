import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "2.4.20"
    id("org.jetbrains.compose") version "1.12.1"
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.20"
    id("org.jetbrains.kotlinx.kover") version "0.9.9"
}

group = "ar.edu.monedero"
version = "1.0"

repositories {
    mavenCentral()
    google()
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.compose.material3:material3:1.9.0")
    implementation("org.jetbrains.compose.material:material-icons-core:1.7.3")

    testImplementation("io.kotest:kotest-runner-junit5:6.2.5")
    testImplementation("io.kotest:kotest-assertions-core:6.2.5")
    testImplementation("org.jetbrains.compose.ui:ui-test:1.12.1")
}

tasks.test {
    useJUnitPlatform()
    // Los tests de UI montan la pantalla en una escena offscreen; esto asegura que nunca abran una ventana.
    systemProperty("java.awt.headless", "true")
    testLogging {
        events("passed", "failed", "skipped")
    }
}

kover {
    reports {
        filters {
            excludes {
                // La raíz de composición (main + Window) sólo se puede ejecutar abriendo una ventana real.
                classes("ar.edu.monedero.ui.MainKt", "ar.edu.monedero.ui.ComposableSingletons\$MainKt")
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "ar.edu.monedero.ui.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "monedero"
            packageVersion = "1.0.0"
        }
    }
}
