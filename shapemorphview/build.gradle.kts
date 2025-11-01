import java.util.Base64

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
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}


tasks.register("decodeSecretKey") {
    val encoded = System.getenv("SIGNING_SECRET_KEY_RING_FILE")
    if (encoded != null) {
        val decoded = Base64.getDecoder().decode(encoded)
        val file = file("secret.gpg")
        file.writeBytes(decoded)
    }
}

tasks.named("publish").configure {
    dependsOn("decodeSecretKey")
}


mavenPublishing {
    coordinates("com.dertefter.shapemorphview", "shapemorphview", "0.0.2")

    publishToMavenCentral()
    signAllPublications()

    pom {
        name.set("ShapeMorphView")
        description.set("Custom view for shape morphing animation.")
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
