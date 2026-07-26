plugins {
    id("com.android.application")
}

android {
    namespace = "oi.masksu.com.test"

    defaultConfig {
        applicationId = "oi.masksu.com.test"
        versionCode = 1
        versionName = "1.0"
        proguardFile("proguard-rules.pro")
    }

    buildTypes {
        release {
            optimization.enable = true
            isShrinkResources = false
        }
    }
}

setupTestApk()

dependencies {
    implementation(libs.test.runner)
    implementation(libs.test.rules)
    implementation(libs.test.junit)
    implementation(libs.test.uiautomator)
}
