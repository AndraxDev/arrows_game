plugins {
    id("arrows.android.feature")
}

android {
    namespace = "dev.andrax.arrows.feature.home"
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    implementation(libs.koin.android)
    api(project(":core:ui"))
    api(project(":data"))
    api(project(":domain"))
    api(project(":core:resources"))
    testImplementation(project(":core:testing"))
    testImplementation(libs.appyx.testing.unit.common)
    testImplementation(libs.appyx.testing.junit5)
}
