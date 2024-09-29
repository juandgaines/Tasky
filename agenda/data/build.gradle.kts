plugins {
    alias(libs.plugins.tasky.android.library)
    alias(libs.plugins.tasky.android.room)
    alias(libs.plugins.tasky.jvm.retrofit)
}

android {
    namespace = "com.juandgaines.agenda.data"
}

dependencies {
    implementation(projects.agenda.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
}