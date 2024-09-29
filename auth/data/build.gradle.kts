plugins {
    alias(libs.plugins.tasky.android.library)
    alias(libs.plugins.tasky.jvm.retrofit)
}
android {
    namespace = "com.juandgaines.auth.data"
}
dependencies {
    implementation(projects.auth.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
}