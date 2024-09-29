import com.android.build.api.dsl.ApplicationExtension
import com.juandgaines.convention.ExtensionType
import com.juandgaines.convention.configureBuildTypes
import com.juandgaines.convention.configureKotlinAndroid
import com.juandgaines.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin :Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("com.google.dagger.hilt.android")
                apply("com.google.devtools.ksp")
            }

            extensions.configure<ApplicationExtension>(){
                defaultConfig{
                    applicationId = libs.findVersion("projectApplicationId").get().toString()
                    targetSdk = libs.findVersion("projectTargetSdkVersion").get().toString().toInt()
                    compileSdk = libs.findVersion("projectCompileSdkVersion").get().toString().toInt()
                    versionCode = libs.findVersion("projectVersionCode").get().toString().toInt()
                    versionName = libs.findVersion("projectVersionName").get().toString()
                }
                configureKotlinAndroid(this)
                configureBuildTypes(this, ExtensionType.APPLICATION)
            }

            dependencies{
                "implementation"(libs.findLibrary("dagger.hilt").get())
                "ksp"(libs.findLibrary("dagger.hilt.compiler").get())
            }
        }
    }
}