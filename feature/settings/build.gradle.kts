plugins {
    id("arrows.android.feature")
    alias(libs.plugins.aboutlibraries)
}

android {
    namespace = "dev.andrax.arrows.feature.settings"
    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        release {
            buildConfigField("Boolean", "DRAW_DEBUG_STUFF", "false")
        }
        debug {
            buildConfigField("Boolean", "DRAW_DEBUG_STUFF", "false")
        }
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

aboutLibraries {
    // Plugin will auto-generate aboutlibraries.json
}

dependencies {
    implementation(libs.koin.android)
    implementation(libs.aboutlibraries.compose.m3)
    implementation(project(":feature:home"))
    implementation(project(":core:ui"))
    implementation(project(":core:resources"))
}
