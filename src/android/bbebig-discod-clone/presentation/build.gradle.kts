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
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.appcompat)

    // coroutine
    implementation(libs.bundles.coroutine)
    // hilt navigation compose
    implementation(libs.hilt.navigation.compose)
    // navigation compose
    implementation(libs.navigation.compose)
    // compose coil
    implementation(libs.coil.compose)
    // compose material2
    implementation(libs.material)
}
