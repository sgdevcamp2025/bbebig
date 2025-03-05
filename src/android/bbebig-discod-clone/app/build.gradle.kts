plugins {
    alias(libs.plugins.bbebig.android.application)
    alias(libs.plugins.bbebig.android.application.compose)
    alias(libs.plugins.bbebig.hilt)
}

android {
    namespace = "com.smilegate.devcamp.app"
    defaultConfig {
        applicationId = "com.smilegate.devcamp.bbebig"
        versionCode = 1
        versionName = "0.0.1" // X.Y.Z; X = Major, Y = minor, Z = Patch level
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))
}
