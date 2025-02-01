plugins {
    application
    alias(libs.plugins.anotherBootPlugin)
    `maven-publish`
    alias(libs.plugins.gradleReleaePlugin)
    alias(libs.plugins.lombokPlugin)
    alias(libs.plugins.openRewritePlugin)
    alias(libs.plugins.graalvmGradlePlugin)
}
var springActiveProfiles: String = providers.gradleProperty("spring.profile").getOrElse("local")
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
    testLogging {
        showStandardStreams = true
    }
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

dependencies {
    implementation(libs.fantasyFootballModel)
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.framework.another.boot:another-boot-starter-webflux")
    implementation("com.framework.another.boot:another-boot-observability-starter")
    implementation("org.postgresql:r2dbc-postgresql")
    implementation("io.r2dbc:r2dbc-pool")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.liquibase:liquibase-core")
    implementation("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.r2dbc:r2dbc-h2")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation(libs.reactorBlockhound)
}

tasks.beforeReleaseBuild {
    dependsOn(tasks.publish)
}

tasks.processAot {
    jvmArgs("-Dspring.profiles.active=$springActiveProfiles")
}

tasks.bootBuildImage {
    // Don't want to provide the gradle property in local and want the image name to be only project.name:project version
    val repo: String = when (providers.gradleProperty("docker.repo").isPresent) {
        true -> "${providers.gradleProperty("docker.repo").get()}/"
        false -> ""
    }
    docker {
        // Since I want to use podman engine when building image in local
        val isPodManHostConfigured = providers.gradleProperty("podman.host")
        if (isPodManHostConfigured.isPresent) {
            host.set(isPodManHostConfigured.get())
            bindHostToBuilder.set(true)
        } else {
            publishRegistry {
                url.set(providers.gradleProperty("docker.registry.url"))
                username.set(providers.gradleProperty("docker.registry.username"))
                password.set(providers.gradleProperty("docker.registry.password"))
            }
        }
    }
    imageName.set("$repo${project.name}:${project.version}")
    environment.put("BPE_SPRING_PROFILES_ACTIVE", springActiveProfiles)
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

rewrite {
    activeRecipe("org.openrewrite.java.OrderImports")
}