plugins {
    alias(libs.plugins.bbebig.hilt)
    alias(libs.plugins.bbebig.android.library)
    alias(libs.plugins.bbebig.android.library.compose)
}

android {
    namespace = "com.smilegate.devcamp.presentation"
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.bundles.coroutine)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.coil.compose)
}
