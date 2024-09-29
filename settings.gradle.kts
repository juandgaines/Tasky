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

rootProject.name = "Tasky"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":core:domain")
include(":core:data")
include(":auth:domain")
include(":auth:presentation")
include(":auth:data")
include(":task:data")
include(":task:domain")
include(":task:presentation")
include(":core:presentation:ui")
include(":core:presentation:designsystem")
