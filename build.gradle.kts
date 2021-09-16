import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig
import java.lang.System.getenv

val artifactoryUrl =
    uri((getenv("ARTIFACTORY_URL") ?: "https://tixartifactory.jfrog.io/tixartifactory").trimEnd('/') + "/")
val generatedClientArtifactId = "ng-testing-allure-junit-platform"
val allureVersion = "2.14.0"

group = "com.tradeix.infrastructure"
version = "2.14.0"

plugins {
    java
    `java-library`
    `maven-publish`
    id("com.jfrog.artifactory") version "4.21.0"
}


repositories {
    mavenCentral()
}


dependencies {
    api("io.qameta.allure:allure-java-commons:$allureVersion")
    implementation("io.qameta.allure:allure-test-filter:$allureVersion")
    implementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    implementation("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    enabled = false
}

tasks.jar {
    from("src/main/services") {
        into("META-INF/services")
    }
}

publishing {
    publications {
        create<MavenPublication>(generatedClientArtifactId) {
            groupId = group.toString()
            artifactId = generatedClientArtifactId
            version = version

            from(components["java"])

        }
    }
}

artifactory {
    setContextUrl(artifactoryUrl)
    publish(delegateClosureOf<PublisherConfig> {

        repository(delegateClosureOf<groovy.lang.GroovyObject> {
            setProperty("repoKey", "generic-local")
            setProperty("username", getenv("ARTIFACTORY_USR"))
            setProperty("password", getenv("ARTIFACTORY_PSW"))
            setProperty("maven", true)
        })

        defaults(delegateClosureOf<groovy.lang.GroovyObject> {
            invokeMethod("publications", generatedClientArtifactId)
            setProperty("publishArtifacts", "true")
            setProperty("publishPom", "true")
        })
    })
}