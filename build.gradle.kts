plugins {
    id("java-library")
    id("jacoco")
    id("idea")
    id("eclipse")
    id("maven-publish")
    id("org.cadixdev.licenser") version "0.6.1"
}

group = "com.github.clubobsidian"
version = "4.1.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

license {
    header.set(resources.text.fromFile(file("HEADER.txt")))
    include("**/*.java")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
        }
    }
}

tasks {
    jacocoTestReport {
        reports {
            xml.required = true
            html.required = false
        }
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
        options.release.set(11)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
    }

    test {
        useJUnitPlatform()
    }

}


repositories {
    mavenCentral()
}

var configurateVersion = "4.1.2"
var junitVersion = "5.9.1"

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    api("org.spongepowered:configurate-yaml:$configurateVersion")
    api("org.spongepowered:configurate-jackson:$configurateVersion")
    api("org.spongepowered:configurate-hocon:$configurateVersion")
    api("org.spongepowered:configurate-xml:$configurateVersion")
}

