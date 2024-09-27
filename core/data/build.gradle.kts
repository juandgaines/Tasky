plugins {
    alias(libs.plugins.tasky.android.library)
}

android {
    namespace = "com.juandgaines.core.data"
}

dependencies {
    implementation(projects.core.domain)
}