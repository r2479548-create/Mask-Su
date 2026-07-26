plugins {
    alias(libs.plugins.android.application)
}

setupCommon()

android {
    namespace = "oi.masksu.com"
    enableKotlin = false

    buildTypes {
        release {
            isShrinkResources = false
        }
    }
}
