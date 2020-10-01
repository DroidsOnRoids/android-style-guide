import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.ben-manes.versions") version Versions.versionsPlugin
    id("io.gitlab.arturbosch.detekt") version Versions.detekt
}

allprojects {
    repositories {
        mavenCentral()
        google()
        jcenter()
    }

    tasks.withType(KotlinCompile::class) {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

dependencies {
    detekt(Libraries.detektFormatting)
    detekt(Libraries.detektCli)
}

tasks.withType<Detekt> {
    config.setFrom(rootProject.file("detekt-config.yml"))
    setSource(files(projectDir))
    exclude(subprojects.map { "${it.buildDir.relativeTo(rootDir).path}/" })
    parallel = true
    reports {
        xml.enabled = true
        html.enabled = false
        txt.enabled = false
    }
}

val check by tasks.registering {
    group = "verification"
    dependsOn(tasks.named("detekt"))
}
