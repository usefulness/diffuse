-dontobfuscate
-keepattributes SourceFile, LineNumberTable

-allowaccessmodification

-keep class com.jakewharton.diffuse.Diffuse {
  public static void main(java.lang.String[]);
}

##############
### APKSIG ###
##############

# Keeps generic signatures for reflection. It requires InnerClasses which requires EnclosingMethod.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Annotations for reflection.
-keepattributes RuntimeVisible*Annotations,AnnotationDefault
-keep class com.android.apksig.internal.** { *; }

##############
### APKSIG ###
##############
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

##############
### Clikt ###
##############
-keep class com.sun.jna.** { *; }
-keep class * implements com.sun.jna.** { *; }
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations,RuntimeVisibleTypeAnnotations,AnnotationDefault
-dontwarn org.graalvm.**
-dontwarn com.oracle.svm.core.annotate.Delete
