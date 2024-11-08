plugins {
    alias(libs.plugins.tasky.android.library)
    alias(libs.plugins.tasky.android.room)
    alias(libs.plugins.tasky.jvm.retrofit)
    id("kotlin-parcelize")
}

android {
    namespace = "com.juandgaines.agenda.data"
}

dependencies {
    implementation(projects.agenda.domain)
    implementation(projects.core.data)
    implementation(projects.core.domain)

    implementation(libs.androidx.work)

    implementation(libs.dagger.hilt.work)
    ksp(libs.dagger.hilt.work.processor)
}