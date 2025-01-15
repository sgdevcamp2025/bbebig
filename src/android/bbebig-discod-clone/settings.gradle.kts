pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "bbebig"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":data")
include(":domain")
include(":presentation")
