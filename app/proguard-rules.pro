# Keep Hilt generated classes
-keep class dagger.hilt.internal.aggregatedroot.codegen.* { *; }
-keep class dagger.hilt.internal.processedrootsentinel.codegen.* { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }

# Keep Kotlin serialization
-keepclassmembers class kotlinx.serialization.** { *; }
-keepclassmembers class com.flowpulse.app.** { @kotlinx.serialization.Serializable *; }

# Keep Room entities and Dao
-keep class androidx.room.** { *; }
-keep interface com.flowpulse.app.data.local.dao.** { *; }

# Keep Compose tooling
-keep class androidx.compose.ui.tooling.** { *; }

# Billing and Ads keys should not be removed
-keep class com.android.billingclient.** { *; }
-keep class com.google.android.gms.ads.** { *; }
