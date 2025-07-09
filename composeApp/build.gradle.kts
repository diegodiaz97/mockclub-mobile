import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    // Kotlin / Multiplatform
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    // Android
    alias(libs.plugins.androidApplication)
    // JetBrains Compose
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    // KSP / Room
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    // Firebase
    alias(libs.plugins.googleServices)
    alias(libs.plugins.firebase.crashlytics)
    // Cocoapods (iOS)
    alias(libs.plugins.kotlinCocoapods)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "15.4"
        podfile = project.file("../iosApp/Podfile")
        pod("GoogleSignIn")
        pod("MapLibre", "6.9.0")

        framework {
            baseName = "composeApp"
            isStatic = true
        }
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Jetpack Compose
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.jetbrains.compose.navigation)
                // AndroidX Lifecycle
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.runtime.compose)
                // Koin
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.koin.core)
                // Kotlinx
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
                // Firebase
                implementation(libs.firebase.firestore)
                implementation(libs.firebase.auth)
                implementation(libs.firebase.storage)
                implementation(libs.firebase.messaging)
                // Room / DB
                implementation(libs.androidx.room.runtime)
                implementation(libs.sqlite.bundled)
                implementation(libs.multiplatform.settings)
                // Ktor
                implementation(libs.bundles.ktor)
                // Coil
                implementation(libs.bundles.coil)
                // UI / Icons / Extras
                implementation(libs.xicon.pack.z.tabler)
                implementation(libs.xicon.pack.z.octicon)
                implementation(libs.xicon.pack.z.font.awesome)
                implementation(libs.reveal.core)
                implementation(libs.reveal.shapes)
                implementation(libs.flexible.bottomsheet.material3)
                implementation(libs.pullrefresh)
                implementation(libs.ui.backhandler)
                implementation(libs.peekaboo.ui)
                implementation(libs.peekaboo.image.picker)
                implementation(libs.zoomable)
                // Others
                implementation(libs.maplibre.compose)
                implementation(libs.passage)
            }
        }

        val androidMain by getting {
            dependencies {
                // Compose Preview & Activity
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)
                // Koin Android
                implementation(libs.koin.android)
                implementation(libs.koin.androidx.compose)
                // Ktor Android
                implementation(libs.ktor.client.okhttp)
                // UI Icons
                implementation(libs.xicon.pack.z.tabler)
                implementation(libs.xicon.pack.z.octicon)
                // Firebase (con BOM)
                implementation(project.dependencies.platform(libs.firebase.bom))
                implementation(libs.com.google.firebase.firebase.auth)
                implementation(libs.com.google.firebase.firebase.firestore)
                implementation(libs.com.google.firebase.firebase.storage)
                implementation(libs.com.google.firebase.firebase.messaging)
                implementation(libs.firebase.crashlytics)
                implementation(libs.firebase.analytics)
                // Permission
                implementation(libs.accompanist.permissions)
            }
        }

        val iosMain by creating {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        dependencies {
            ksp(libs.androidx.room.compiler)
        }
    }
}

android {
    namespace = "com.diego.futty"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.diego.futty"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
