// build.gradle.kts (Module: app)
// Add these dependencies to your existing dependencies block:

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // ViewBinding must be enabled in android {} block:
    // buildFeatures {
    //     viewBinding = true
    // }
}

// In your android {} block, make sure you have:
// buildFeatures {
//     viewBinding = true
// }
