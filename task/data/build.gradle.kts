plugins {
    alias(libs.plugins.tasky.android.library)
}

android {
    namespace = "com.juandgaines.task.data"
}

dependencies {
    implementation(projects.task.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
}