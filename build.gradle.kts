import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.2"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
    kotlin("plugin.jpa") version "1.7.22"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    dependencyManagement {
        imports {
            mavenBom("de.codecentric:spring-boot-admin-dependencies:3.0.2")
            mavenBom("io.micrometer:micrometer-bom:1.10.5")
            mavenBom("io.micrometer:micrometer-tracing-bom:1.0.3")
        }
    }
    val springDocVersion = "2.0.4"
    val queryDslVersion = "5.0.0"
    val mapStructVersion = "1.5.5.Final"

    fun amazon(module: String, version: String? = null) =
        "software.amazon.awssdk:${module}${version?.let { ":$it" } ?: ""}"

    implementation(amazon("secretsmanager", "2.20.16"))
    implementation(amazon("ssm", "2.20.16"))
    implementation(amazon("elasticbeanstalk", "2.20.16"))
    implementation(amazon("s3", "2.20.16"))

    implementation("org.springframework.boot", "spring-boot-starter-actuator")
    implementation("org.springframework.boot", "spring-boot-starter-batch")
    implementation("org.springframework.boot", "spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot", "spring-boot-starter-data-jpa")
    implementation("org.springframework.boot", "spring-boot-starter-security")
    implementation("org.springframework.boot", "spring-boot-starter-validation")
    implementation("org.springframework.boot", "spring-boot-starter-web")
    implementation("org.springframework.boot", "spring-boot-configuration-processor")

    implementation("org.postgresql", "postgresql", "42.6.0")

    implementation("org.springframework.cloud", "spring-cloud-starter-aws", "2.2.6.RELEASE")
    implementation("org.springframework.cloud:spring-cloud-starter-aws-messaging:2.2.1.RELEASE")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.0.1")

    implementation("de.codecentric", "spring-boot-admin-starter-client")
    implementation("de.codecentric", "spring-boot-admin-starter-server")

    implementation("org.springdoc", "springdoc-openapi-starter-common", springDocVersion)
    implementation("org.springdoc", "springdoc-openapi-starter-webmvc-ui", springDocVersion)

    implementation("io.micrometer", "micrometer-observation")
    implementation("io.micrometer", "micrometer-tracing")
    implementation("io.micrometer", "micrometer-tracing-bridge-brave")
    implementation("io.micrometer", "context-propagation")
    // implementation("io.micrometer:micrometer-registry-prometheus")
    // implementation("io.zipkin.reporter2:zipkin-reporter-brave")

    implementation("org.jetbrains.kotlin", "kotlin-reflect", "1.8.10")
    implementation("org.jetbrains.kotlin", "kotlin-stdlib", "1.8.10")

    implementation("com.querydsl", "querydsl-jpa", queryDslVersion, classifier = "jakarta")
    implementation("com.querydsl", "querydsl-kotlin", queryDslVersion)
    implementation("jakarta.persistence", "jakarta.persistence-api")
    implementation("jakarta.annotation", "jakarta.annotation-api")

    implementation("org.mapstruct", "mapstruct", mapStructVersion)

    implementation("com.google.guava", "guava", "31.1-jre")
    api("com.google.guava", "guava", "31.1-jre")

    implementation("org.apache.poi", "poi", "5.2.3")
    implementation("org.apache.poi", "poi-ooxml", "5.2.3")

    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin")
    implementation("io.github.microutils", "kotlin-logging-jvm", "3.0.5")
    implementation("com.auth0", "java-jwt", "4.3.0")
    implementation("org.reflections", "reflections", "0.10.2")
    implementation("com.vladmihalcea", "hibernate-types-60", "2.21.1")

    implementation("com.h2database", "h2")

    developmentOnly("org.springframework.boot", "spring-boot-devtools")
    annotationProcessor("org.springframework.boot", "spring-boot-configuration-processor")

    testRuntimeOnly("com.h2database", "h2")
    testRuntimeOnly("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.6.4")
    testRuntimeOnly("org.jetbrains.kotlinx", "kotlinx-coroutines-test", "1.6.4")
    testImplementation("org.springframework.boot", "spring-boot-starter-test")
    testImplementation("org.springframework", "spring-webflux", "6.0.6")
    testImplementation("org.springframework.batch", "spring-batch-test")
    testImplementation("org.springframework.graphql", "spring-graphql-test")
    testImplementation("org.springframework.security", "spring-security-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}