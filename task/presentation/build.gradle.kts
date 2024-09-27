plugins {
    alias(libs.plugins.tasky.android.library.compose)
}

android {
    namespace = "com.juandgaines.task.presentation"
}

dependencies {
    implementation(projects.task.domain)
    implementation(projects.core.domain)
    implementation(projects.core.presentation)
}