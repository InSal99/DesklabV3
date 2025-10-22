plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("org.jetbrains.kotlin.kapt")
}

group = "com.edts.components"
version = "v0.12.0"
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.edts.components"
                artifactId = "components"
                version = "v0.12.0"

                pom {
                    name.set("Desklab Components Library")
                    description.set("A UI component library designed specifically for Desklab Project Android applications.")
                    url.set("https://github.com/InSal99/DesklabV3")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }

                    developers {
                        developer {
                            id.set("InSal99")
                            name.set("Intan Saliya Utomo")
                            email.set("intan.saliya@sg-dsa.com")
                        }
                        developer {
                            id.set("Yovita Handayiani")
                            name.set("Yovita Handayiani")
                            email.set("yovita.handayiani@sg-dsa.com")
                        }
                        developer {
                            id.set("fauzanspratama")
                            name.set("Fauzan Sukmapratama")
                            email.set("fauzan.sukmapratama@sg-dsa.com")
                        }
                    }
                }
            }
        }
    }
}


android {
    namespace = "com.edts.components"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    configurations.all {
        resolutionStrategy {
            force("org.jetbrains.kotlin:kotlin-stdlib:1.7.10")
            force("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.7.10")
            force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.10")
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.library)
    implementation(libs.glide)
    kapt(libs.glide.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}