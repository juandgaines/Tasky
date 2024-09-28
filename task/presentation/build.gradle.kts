plugins {
    alias(libs.plugins.tasky.android.feature.ui)
}

android {
    namespace = "com.juandgaines.task.presentation"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.androidx.activity.compose)

    implementation(projects.task.domain)
    implementation(projects.core.domain)
    implementation(projects.core.presentation)
}