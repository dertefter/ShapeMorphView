plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
    id("com.vanniktech.maven.publish") version "0.34.0"
}

android {
    namespace = "com.dertefter.shapemorphview"
    compileSdk {
        version = release(36)
    }

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

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}

publishing {
    publications {

        create<MavenPublication>("release") {
            groupId = "com.dertefter.shapemorphview"
            artifactId = "shapemorphview"
            version = "0.0.1"

            afterEvaluate {
                from(components["release"])
            }
        }

    }

    repositories{
        mavenLocal()
    }

}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()
}
