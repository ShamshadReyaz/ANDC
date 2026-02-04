########## HILT / DAGGER ##########
# Keep Hilt and Dagger internals
-keep class dagger.hilt.** { *; }
-keep class dagger.** { *; }
-keep class javax.inject.** { *; }

# Keep Hilt generated classes (activities, fragments, injectors)
-keep class *Hilt* { *; }
-keep class *GeneratedInjector { *; }
-keep class **_HiltModules_* { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }
-keep class * extends dagger.hilt.internal.GeneratedEntryPoint { *; }

# Keep your app's @AndroidEntryPoint Activities, Fragments, and Application
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }
-keep @dagger.hilt.android.HiltAndroidApp class * { *; }

# Keep Application subclass
-keep class com.mobiquel.dyalsinghapp.** extends android.app.Application { *; }

# Preserve annotations (required by Hilt)
-keepattributes *Annotation*, Signature

########## OPTIONAL (if using ViewModels) ##########
-keep class androidx.hilt.lifecycle.** { *; }
-keep class androidx.lifecycle.DefaultViewModelProviderFactory { *; }

########## ACTIVITY AND FRAGMENT PROTECTION ##########
# Keep all Activity classes
-keep class com.mobiquel.dyalsinghapp.view.** extends android.app.Activity { *; }
-keep class com.mobiquel.dyalsinghapp.view.** extends androidx.appcompat.app.AppCompatActivity { *; }

# Keep all Fragment classes
-keep class com.mobiquel.dyalsinghapp.view.fragment.** extends androidx.fragment.app.Fragment { *; }

# Keep ViewBinding classes
-keep class com.mobiquel.dyalsinghapp.databinding.** { *; }

########## INTENT AND NAVIGATION PROTECTION ##########
# Keep Intent classes and methods
-keep class android.content.Intent { *; }
-keep class android.content.ComponentName { *; }

# Keep Parcelable classes
-keep class * implements android.os.Parcelable { *; }
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}

########## JSON AND API PROTECTION ##########
# Keep JSON classes
-keep class org.json.** { *; }
-keep class com.google.gson.** { *; }

# Keep Retrofit and OkHttp classes
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class com.squareup.okhttp3.** { *; }

# Keep API response classes
-keep class com.mobiquel.dyalsinghapp.pojo.** { *; }
-keep class com.mobiquel.dyalsinghapp.data.** { *; }

########## REFLECTION AND RUNTIME PROTECTION ##########
# Keep classes that use reflection
-keep class com.mobiquel.dyalsinghapp.utils.** { *; }

# Keep BroadcastReceiver classes
-keep class com.mobiquel.dyalsinghapp.** extends android.content.BroadcastReceiver { *; }

# Keep Service classes
-keep class com.mobiquel.dyalsinghapp.** extends android.app.Service { *; }

########## FIREBASE PROTECTION ##########
# Keep Firebase classes
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# Keep Firebase messaging service
-keep class com.mobiquel.dyalsinghapp.firebaseservices.** { *; }

########## ROOM DATABASE PROTECTION ##########
# Keep Room database classes
-keep class com.mobiquel.dyalsinghapp.room.** { *; }
-keep class androidx.room.** { *; }

########## ADDITIONAL PROTECTION ##########
# Keep all classes in your main package
-keep class com.mobiquel.dyalsinghapp.** { *; }

# Keep all native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep all enum classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep Serializable classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}