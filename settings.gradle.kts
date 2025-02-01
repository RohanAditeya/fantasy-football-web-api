rootProject.name = "fantasy-football-web-api"

pluginManagement {

    repositories {
        gradlePluginPortal()
        google()
        mavenLocal()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/RohanAditeya/another-framework")
            credentials {
                username = providers.gradleProperty("artifactory.user").get()
                password = providers.gradleProperty("artifactory.password").get()
            }
        }
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.framework.another.boot")
                useModule("${requested.id.id}:another-boot-gradle-plugin:${requested.version}")
        }
    }
}