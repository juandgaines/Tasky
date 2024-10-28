plugins {
    alias(libs.plugins.tasky.android.feature.ui)
}

android {
    namespace = "com.juandgaines.agenda.presentation"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.androidx.activity.compose)

    implementation(projects.core.domain)
    implementation(projects.agenda.domain)
}