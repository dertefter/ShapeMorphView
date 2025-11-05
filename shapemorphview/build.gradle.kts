
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.vanniktech.maven.publish") version "0.34.0"
}

android {
    namespace = "com.dertefter.shapemorphview"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
}

mavenPublishing {
    coordinates("io.github.dertefter", "shapemorphview", "0.0.4")

    publishToMavenCentral()
    signAllPublications()

    pom {
        name.set("ShapeMorphView")
        description.set("Custom view for shape morphing animation. Supports Material 3 Expressive shapes and shape morphing")
        url.set("https://github.com/dertefter/ShapeMorphView")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
            }
        }
        developers {
            developer {
                id.set("dertefter")
                name.set("Dertefter Labs")
                url.set("https://github.com/dertefter")
            }
        }
        scm {
            connection.set("scm:git:https://github.com/dertefter/ShapeMorphView.git")
            developerConnection.set("scm:git:ssh://github.com/dertefter/ShapeMorphView.git")
            url.set("https://github.com/dertefter/ShapeMorphView")
        }
    }
}

