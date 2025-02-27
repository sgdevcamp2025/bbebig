plugins {
    alias(libs.plugins.bbebig.android.library)
    alias(libs.plugins.bbebig.hilt)
    alias(libs.plugins.bbebig.android.room)
}

android {
    namespace = "com.smilegate.devcamp.data"
}

dependencies {
    // coroutine bundle
    implementation(libs.bundles.coroutine)
    // retrofit bundle
    implementation(libs.bundles.network)
    // data store preferences
    implementation(libs.androidx.datastore.preferences)
    // encrypted shared preferences
    implementation(libs.androidx.security.crypto)
    // krossbow stomp
    implementation(libs.krossbow.stomp.core)
    implementation(libs.krossbow.websocket.okhttp)
    implementation(libs.krossbow.stomp.kxserialization)
    implementation(libs.krossbow.stomp.kxserialization.json)
}
