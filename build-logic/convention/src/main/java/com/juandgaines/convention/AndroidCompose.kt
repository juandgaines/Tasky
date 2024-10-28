package com.juandgaines.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>
){
    commonExtension.run {
        buildFeatures {
            compose = true
        }
    }
    dependencies {
        val bom = libs.findLibrary("androidx.compose.bom").get()
        val toolingPreview = libs.findLibrary("androidx.ui.tooling.preview").get()
        "implementation"(libs.findLibrary("kotlinx.serialization.json").get())
        "implementation"(platform(bom))
        "androidTestImplementation"(platform(bom))
        "debugImplementation"(toolingPreview)
    }
}