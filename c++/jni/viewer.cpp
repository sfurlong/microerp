#include "viewer.h"

JNIEXPORT jboolean JNICALL Java_viewer_closePrintJob
  (JNIEnv *, jobject){
   return true;
}
JNIEXPORT jboolean JNICALL Java_viewer_openPreviewWindow
  (JNIEnv *, jobject){
   return true;
}
JNIEXPORT jboolean JNICALL Java_viewer_openPrintJob
  (JNIEnv *, jobject){
   return true;
}

