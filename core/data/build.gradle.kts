plugins {
    alias(libs.plugins.tasky.android.library)
    alias(libs.plugins.tasky.android.room)
    alias(libs.plugins.tasky.jvm.retrofit)
}

android {
    namespace = "com.juandgaines.core.data"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(projects.core.domain)
    implementation(libs.datastore.preferences)
}