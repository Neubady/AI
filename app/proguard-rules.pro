# FlowPulse for n8n - R8 rules
-dontnote kotlinx.serialization.**
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class kotlinx.serialization.** { *; }
-keepclasseswithmembers class * {
    @kotlinx.serialization.Serializable *;
}

-keep class com.google.firebase.messaging.** { *; }
-keep class com.google.android.gms.ads.** { *; }
-keep class com.android.billingclient.** { *; }
-keep class androidx.room.** { *; }
-keep class androidx.work.** { *; }
-keep class androidx.navigation.** { *; }
-keep class dagger.hilt.** { *; }
-keep class dagger.hilt.internal.** { *; }

-keepattributes Signature, InnerClasses, EnclosingMethod, RuntimeVisibleAnnotations, RuntimeInvisibleAnnotations

-assumenosideeffects class timber.log.Timber {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

-if class com.flowpulse.app.BuildConfig { boolean ALLOW_HTTP_IN_DEBUG; }
-keep class com.flowpulse.app.BuildConfig { *; }

# Room schema
-keep class **Database_Impl { *; }

# Retrofit and OkHttp keep models
-keepclassmembers class * {
    @retrofit2.http.* <methods>;
}

# Compose preview
-keep class androidx.compose.ui.tooling.preview.PreviewParameterProvider { *; }
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.Nonnull
