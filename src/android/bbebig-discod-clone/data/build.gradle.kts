plugins {
    alias(libs.plugins.bbebig.android.library)
    alias(libs.plugins.bbebig.hilt)
    alias(libs.plugins.bbebig.android.room)
}

android {
    namespace = "com.smilegate.devcamp.data"
}

dependencies {
    implementation(libs.bundles.coroutine)
    implementation(libs.bundles.network)
    ksp(libs.moshi.codegen)
}
