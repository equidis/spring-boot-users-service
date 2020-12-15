import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val commonsVersion: String by project
val springCloudVersion: String by project

plugins {
    id("org.springframework.boot") version "2.4.0"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
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

extra["springCloudVersion"] = springCloudVersion

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
    implementation("com.github.jntakpe:sb-commons-cache:$commonsVersion")
    implementation("com.github.jntakpe:sb-commons-mongo:$commonsVersion")
    implementation("com.github.jntakpe:sb-commons-web:$commonsVersion")
    testImplementation("com.github.jntakpe:sb-commons-cache-test:$commonsVersion")
    testImplementation("com.github.jntakpe:sb-commons-mongo-test:$commonsVersion")
    testImplementation("com.github.jntakpe:sb-commons-test:$commonsVersion")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
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

