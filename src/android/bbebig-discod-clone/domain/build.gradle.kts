plugins {
    alias(libs.plugins.bbebig.android.library)
    alias(libs.plugins.bbebig.hilt)
}

android {
    namespace = "com.smilegate.devcamp.domain"
}

dependencies {
    implementation(project(":data"))
    implementation(libs.krossbow.stomp.kxserialization)
    implementation(libs.krossbow.stomp.kxserialization.json)
}
