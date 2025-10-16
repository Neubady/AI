plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.flowpulse.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.flowpulse.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        buildConfigField("Boolean", "ALLOW_HTTP_IN_DEBUG", "${project.findProperty("ALLOW_HTTP_IN_DEBUG") ?: "true"}")
        buildConfigField("Int", "FREE_INSTANCE_LIMIT", "${project.findProperty("DEFAULT_INSTANCE_LIMIT_FREE") ?: "1"}")
        buildConfigField("Int", "POLL_INTERVAL_MINUTES", "${project.findProperty("DEFAULT_POLL_INTERVAL_MINUTES") ?: "30"}")
        buildConfigField("String", "ADMOB_BANNER_UNIT_ID", "\"${project.findProperty("ADMOB_BANNER_UNIT_ID") ?: ""}\"")
        buildConfigField("String", "ADMOB_INTERSTITIAL_UNIT_ID", "\"${project.findProperty("ADMOB_INTERSTITIAL_UNIT_ID") ?: ""}\"")
        buildConfigField("String", "CERTIFICATE_PIN_SHA256", "\"${project.findProperty("CERTIFICATE_PIN_SHA256") ?: ""}\"")
        buildConfigField("String", "BILLING_MONTHLY_PRODUCT_ID", "\"${project.findProperty("BILLING_MONTHLY_PRODUCT_ID") ?: "flowpulse.pro.monthly"}\"")
        buildConfigField("String", "BILLING_LIFETIME_PRODUCT_ID", "\"${project.findProperty("BILLING_LIFETIME_PRODUCT_ID") ?: "flowpulse.pro.lifetime"}\"")
    }

    signingConfigs {
        create("release") {
            val keystorePath = project.findProperty("RELEASE_STORE_FILE") as String?
            val keystorePassword = project.findProperty("RELEASE_STORE_PASSWORD") as String?
            val keyAlias = project.findProperty("RELEASE_KEY_ALIAS") as String?
            val keyPassword = project.findProperty("RELEASE_KEY_PASSWORD") as String?
            storeFile = keystorePath?.let { file(it) }
            storePassword = keystorePassword
            this.keyAlias = keyAlias
            this.keyPassword = keyPassword
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isMinifyEnabled = false
            isDebuggable = true
            manifestPlaceholders["appLabelSuffix"] = " (Debug)"
            manifestPlaceholders["admobAppId"] = project.findProperty("APP_ADMOB_APP_ID") ?: "ca-app-pub-3940256099942544~3347511713"
            manifestPlaceholders["admobBannerId"] = project.findProperty("ADMOB_BANNER_UNIT_ID") ?: "ca-app-pub-3940256099942544/6300978111"
            manifestPlaceholders["admobInterstitialId"] = project.findProperty("ADMOB_INTERSTITIAL_UNIT_ID") ?: "ca-app-pub-3940256099942544/1033173712"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders["appLabelSuffix"] = ""
            manifestPlaceholders["admobAppId"] = project.findProperty("APP_ADMOB_APP_ID") ?: ""
            manifestPlaceholders["admobBannerId"] = project.findProperty("ADMOB_BANNER_UNIT_ID") ?: ""
            manifestPlaceholders["admobInterstitialId"] = project.findProperty("ADMOB_INTERSTITIAL_UNIT_ID") ?: ""
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xjvm-default=all",
            "-opt-in=kotlin.RequiresOptIn"
        )
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    packaging {
        resources {
            excludes += setOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
                "META-INF/LICENSE-notice.txt",
                "META-INF/DEPENDENCIES",
                "META-INF/AL2.0",
                "META-INF/LGPL2.1"
            )
        }
    }

    lint {
        abortOnError = false
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
        unitTests.isIncludeAndroidResources = true
        animationsDisabled = true
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.compose.graphics)
    implementation(libs.androidx.compose.constraintlayout)
    implementation(libs.jvm.datetime)
    implementation(libs.androidx.biometric)
    implementation(libs.accompanist.permissions)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.play.services)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.okhttp.urlconnection)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.security.crypto)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    kapt(libs.androidx.room.compiler)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.work)

    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.work.multiprocess)

    implementation(libs.firebase.messaging)
    implementation(libs.firebase.analytics)

    implementation(libs.mobile.ads)
    implementation(libs.play.billing)
    implementation(libs.play.core)
    implementation(libs.play.oss)

    implementation(libs.mpandroidchart)
    implementation(libs.coil.compose)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.uiautomator)

    testImplementation(libs.junit5.api)
    testImplementation(libs.junit5.params)
    testRuntimeOnly(libs.junit5.engine)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.truth)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.androidx.test.monitor)
    testImplementation(libs.robolectric)
}
