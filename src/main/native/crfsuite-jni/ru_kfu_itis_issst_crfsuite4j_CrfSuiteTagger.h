/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class ru_kfu_itis_issst_crfsuite4j_CrfSuiteTagger */

#ifndef _Included_ru_kfu_itis_issst_crfsuite4j_CrfSuiteTagger
#define _Included_ru_kfu_itis_issst_crfsuite4j_CrfSuiteTagger
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     ru_kfu_itis_issst_crfsuite4j_CrfSuiteTagger
 * Method:    dispose
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_ru_kfu_itis_issst_crfsuite4j_CrfSuiteTagger_dispose
  (JNIEnv *, jobject);

/*
 * Class:     ru_kfu_itis_issst_crfsuite4j_CrfSuiteTagger
 * Method:    init
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_ru_kfu_itis_issst_crfsuite4j_CrfSuiteTagger_init
  (JNIEnv *, jobject);

/*
 * Class:     ru_kfu_itis_issst_crfsuite4j_CrfSuiteTagger
 * Method:    doOpen
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_ru_kfu_itis_issst_crfsuite4j_CrfSuiteTagger_doOpen
  (JNIEnv *, jobject, jstring);

/*
 * Class:     ru_kfu_itis_issst_crfsuite4j_CrfSuiteTagger
 * Method:    doTag
 * Signature: ([[Lru/kfu/itis/issst/crfsuite4j/Attribute;)[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_ru_kfu_itis_issst_crfsuite4j_CrfSuiteTagger_doTag
  (JNIEnv *, jobject, jobjectArray);

#ifdef __cplusplus
}
#endif
#endif
