import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.googleServices)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")

    room {
        schemaDirectory("$projectDir/schemas")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.runtime.compose)

                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.runtime.compose)
                implementation(libs.jetbrains.compose.navigation)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.androidx.room.runtime)
                implementation(libs.sqlite.bundled)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                api(libs.koin.core)

                implementation(libs.bundles.ktor)
                implementation(libs.bundles.coil)
                implementation(libs.xicon.pack.z)
                implementation(libs.kotlinx.datetime)
                implementation(libs.firebase.firestore)
                implementation(libs.firebase.auth)
                implementation(libs.firebase.storage)
                implementation(libs.flexible.bottomsheet.material3)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)

                implementation(libs.koin.android)
                implementation(libs.koin.androidx.compose)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.xicon.pack.z)

                implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
                implementation(libs.com.google.firebase.firebase.auth)
                implementation(libs.com.google.firebase.firebase.firestore)
                implementation(libs.com.google.firebase.firebase.storage)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.xicon.pack.z)
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.diego.futty.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.diego.futty"
            packageVersion = "1.0.0"
        }
    }
}
