/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class RptEng */

#ifndef _Included_RptEng
#define _Included_RptEng
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     RptEng
 * Method:    closePrintJob
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_RptEng_closePrintJob
  (JNIEnv *, jobject);

/*
 * Class:     RptEng
 * Method:    openPreviewWindow
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_RptEng_openPreviewWindow
  (JNIEnv *, jobject);

/*
 * Class:     RptEng
 * Method:    openPrintJob
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_RptEng_openPrintJob
  (JNIEnv *, jobject, jstring);

/*
 * Class:     RptEng
 * Method:    setNthParmField
 * Signature: (ILjava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_RptEng_setNthParmField
  (JNIEnv *, jobject, jint, jstring);

/*
 * Class:     RptEng
 * Method:    startJob
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_RptEng_startJob
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
