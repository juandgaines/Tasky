import com.juandgaines.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class RetrofitConventionPlugin:Plugin<Project>{

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("org.jetbrains.kotlin.plugin.serialization")
            }
            dependencies {
                "implementation"(libs.findBundle("retrofit").get())
            }
        }
    }
}