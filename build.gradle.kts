plugins {
    java
    `java-library`
    `maven-publish`
}

group = "com.tradeix.infrastructure.allure.junitplatform"
version = "2.14.0"

repositories {
    mavenCentral()
}

val allureVersion = "2.14.0"
val jupiterVersion = "5.7.0"

val agent: Configuration by configurations.creating

dependencies {
    agent("org.aspectj:aspectjweaver")
    api("io.qameta.allure:allure-java-commons:$allureVersion")
    implementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
    implementation("org.junit.platform:junit-platform-launcher")
    implementation("io.qameta.allure:allure-test-filter:$allureVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.3")
    testAnnotationProcessor("org.slf4j:slf4j-simple:1.7.30")
    testAnnotationProcessor("io.qameta.allure:allure-descriptions-javadoc:$allureVersion")
    testImplementation("io.github.glytching:junit-extensions:2.4.0")
    testImplementation("org.assertj:assertj-core:3.19.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.slf4j:slf4j-simple:1.7.30")
    testImplementation("io.qameta.allure:allure-assertj:$allureVersion")
    testImplementation("io.qameta.allure:allure-java-commons-test:$allureVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
}

tasks.test {
    enabled = false
}

tasks.jar {
    from("src/main/services") {
        into("META-INF/services")
    }
}


val generatedClientArtifactId = "ng-testing-allure-junit-platform"

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