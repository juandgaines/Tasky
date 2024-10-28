plugins {
    alias(libs.plugins.tasky.android.library.compose)
}

android {
    namespace = "com.juandgaines.core.presentation.ui"
}

dependencies {
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling.preview)

    implementation(projects.core.domain)
    implementation(projects.core.presentation.designsystem)
}