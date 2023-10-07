pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        jcenter()
        maven(url="https://jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven(url="https://jitpack.io")
    }
}

rootProject.name = "Fractals"
include(":app")
include(":app-ui")
include(":app-data")
include(":app-domain")
