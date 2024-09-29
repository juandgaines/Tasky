plugins {
    alias(libs.plugins.tasky.android.library)
    alias(libs.plugins.tasky.android.room)
    alias(libs.plugins.tasky.jvm.retrofit)
}

android {
    namespace = "com.juandgaines.task.data"
}

dependencies {
    implementation(projects.task.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
}