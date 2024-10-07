plugins {
    alias(libs.plugins.tasky.android.feature.ui)
}

android {
    namespace = "com.juandgaines.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.auth.domain)
}