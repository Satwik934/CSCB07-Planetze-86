plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.planetze86"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.planetze86"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation("org.apache.poi:poi-ooxml:5.2.3")
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation("com.hbb20:ccp:2.7.1")
    implementation(libs.firebase.database)
    implementation(libs.mpandroidchart)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    testImplementation ("org.mockito:mockito-core:5.5.0")
    androidTestImplementation(libs.espresso.core)


}

