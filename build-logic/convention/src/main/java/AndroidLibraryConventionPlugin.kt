import com.android.build.api.dsl.LibraryExtension
import com.juandgaines.convention.ExtensionType
import com.juandgaines.convention.configureBuildTypes
import com.juandgaines.convention.configureKotlinAndroid
import com.juandgaines.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin:Plugin<Project>{

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("com.google.dagger.hilt.android")
                apply("com.google.devtools.ksp")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)

                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.LIBRARY
                )

                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }
            }

            dependencies {
                "implementation"(libs.findLibrary("dagger.hilt").get())
                "ksp"(libs.findLibrary("dagger.hilt.compiler").get())
                "testImplementation"(kotlin("test"))
            }
        }
    }

}