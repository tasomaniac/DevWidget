-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

-dontwarn org.jetbrains.annotations.**
-dontwarn com.google.errorprone.annotations.*

-keepnames public interface com.uber.autodispose.lifecycle.CorrespondingEventsFunction { *; }
