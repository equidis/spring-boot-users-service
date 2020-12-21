import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.cloud.contract.verifier.config.TestFramework.JUNIT5
import org.springframework.cloud.contract.verifier.config.TestMode.WEBTESTCLIENT

val commonsVersion: String by project

plugins {
    idea
    `maven-publish`
    jacoco
    id("org.springframework.boot") version "2.4.0"
    id("org.springframework.cloud.contract") version "2.2.5.RELEASE"
    id("com.google.cloud.tools.jib") version "2.7.0"
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.spring") version "1.4.21"
}

group = "com.github.jntakpe"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    mavenGithub("equidis/sb-commons")
    maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
    implementation("com.github.jntakpe:sb-commons-cache:$commonsVersion")
    implementation("com.github.jntakpe:sb-commons-management:$commonsVersion")
    implementation("com.github.jntakpe:sb-commons-mongo:$commonsVersion")
    implementation("com.github.jntakpe:sb-commons-tracing:$commonsVersion")
    implementation("com.github.jntakpe:sb-commons-web:$commonsVersion")
    testImplementation("com.github.jntakpe:sb-commons-cache-test:$commonsVersion")
    testImplementation("com.github.jntakpe:sb-commons-mongo-test:$commonsVersion")
    testImplementation("com.github.jntakpe:sb-commons-test:$commonsVersion")
    testImplementation("com.github.jntakpe:sb-commons-web-test:$commonsVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jib {
    to {
        image = "eu.gcr.io/equidis/springboot-users:${project.version}"
    }
}

tasks {
    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.isEnabled = true
        }
        classDirectories.setFrom(
            sourceSets.main.get().output.asFileTree.matching {
                exclude("build/generated", "**/model/entity/**")
            }
        )
    }
    check {
        dependsOn(jacocoTestReport)
    }
}

contracts {
    setTestFramework(JUNIT5)
    setTestMode(WEBTESTCLIENT)
    setFailOnNoContracts(false)
    setBasePackageForTests("com.github.jntakpe.sbusers")
    setBaseClassForTests("com.github.jntakpe.commons.web.test.ContractBaseClass")
}

publishing {
    repositories {
        mavenGithub("equidis/spring-boot-users-service")
    }
}

fun RepositoryHandler.mavenGithub(repository: String) = maven {
    name = "Github_packages"
    setUrl("https://maven.pkg.github.com/$repository")
    credentials {
        val githubActor: String? by project
        val githubToken: String? by project
        username = githubActor
        password = githubToken
    }
}

