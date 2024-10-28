package com.juandgaines.convention

import org.gradle.api.Project
import org.gradle.internal.impldep.org.h2.command.dml.Explain
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

fun DependencyHandlerScope.addUiLayerDependencies(project: Project) {
    "implementation"(project(":core:presentation:ui"))
    "implementation"(project(":core:presentation:designsystem"))

    "implementation"(project.libs.findLibrary("androidx.navigation.compose").get())
    "implementation"(project.libs.findLibrary("dagger.hilt.navigation.compose").get())


    "implementation"(project.libs.findBundle("compose").get())
    "debugImplementation"(project.libs.findBundle("compose.debug").get())
    "androidTestImplementation"(project.libs.findLibrary("androidx-ui-test-junit4").get())

}