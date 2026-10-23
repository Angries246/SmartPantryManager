Smart Pantry Manager - how to open
1. Android Studio > File > Open > select this SmartPantryManager folder.
2. If asked about Gradle / "Use Gradle wrapper", accept the default. If it asks for a JDK, use the embedded JDK 17.
3. Wait for sync, then Run.
No NDK is needed. If you still see an NDK error, delete any 'ndk.dir' line in local.properties
and remove any 'ndkVersion' line from app/build.gradle.
