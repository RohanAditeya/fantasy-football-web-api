import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    application
    alias(libs.plugins.anotherBootPlugin)
    `maven-publish`
    alias(libs.plugins.gradleReleaePlugin)
    alias(libs.plugins.lombokPlugin)
}

group = "com.framework.another.boot"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    withJavadocJar()
    withSourcesJar()
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("-XX:+AllowRedefinitionToAddDeleteMethods")
}

repositories {
    google()
    mavenLocal()
    mavenCentral()
    maven {
        name = "github-packages"
        url = uri("https://maven.pkg.github.com/RohanAditeya/fantasy-football-model")
        credentials {
            username = providers.gradleProperty("artifactory.user").get()
            password = providers.gradleProperty("artifactory.password").get()
        }
    }
}

the<DependencyManagementExtension>().apply {
    imports {
        mavenBom(libs.springModulithBom.get().toString())
    }
}

dependencies {
    implementation(libs.fantasyFootballModel)
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.framework.another.boot:another-boot-starter-webflux")
    implementation("com.framework.another.boot:another-boot-observability-starter")
    implementation("org.springframework.modulith:spring-modulith-starter-core")
    implementation("org.postgresql:r2dbc-postgresql")
    implementation("io.r2dbc:r2dbc-pool")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.liquibase:liquibase-core")
    implementation("org.postgresql:postgresql")
    implementation("org.springframework.modulith:spring-modulith-events-api")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.r2dbc:r2dbc-h2")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation(libs.reactorBlockhound)
    testImplementation("org.springframework.modulith:spring-modulith-starter-test")
}

tasks.beforeReleaseBuild {
    dependsOn(tasks.publish)
}

publishing {
    publications {
        create<MavenPublication>("maven-publish") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "github-packages"
            url = uri("https://maven.pkg.github.com/RohanAditeya/fantasy-football-web-api")
            credentials {
                username = providers.gradleProperty("artifactory.user").get()
                password = providers.gradleProperty("artifactory.password").get()
            }
        }
    }
}