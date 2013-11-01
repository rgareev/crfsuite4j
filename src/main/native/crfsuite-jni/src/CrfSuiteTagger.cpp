#include <cstdarg>
#include <crfsuite_api.hpp>
#include "commons.h"
#include "attributes.h"
#include "ru_kfu_itis_issst_crfsuite4j_CrfSuiteTagger.h"

using namespace CRFSuite;

JNIEXPORT void JNICALL Java_ru_kfu_itis_issst_crfsuite4j_CrfSuiteTagger_init(JNIEnv *env, jobject obj){
	Tagger *tagger = new Tagger;
	setHandle(env, obj, tagger);
}

JNIEXPORT void JNICALL Java_ru_kfu_itis_issst_crfsuite4j_CrfSuiteTagger_dispose(JNIEnv *env, jobject obj){
	Tagger *tagger = getHandle<Tagger>(env, obj);
	setHandle<jlong>(env, obj, 0);
	delete tagger;
}

JNIEXPORT jboolean JNICALL Java_ru_kfu_itis_issst_crfsuite4j_CrfSuiteTagger_doOpen(JNIEnv *env, jobject obj, jstring jModelPath){
	Tagger *tagger = getHandle<Tagger>(env, obj);
	std::string modelPath = toStdString(env, jModelPath);
	return tagger->open(modelPath) ? JNI_TRUE : JNI_FALSE;
	// TODO handle C++ exceptions and send them to Java
}

JNIEXPORT jobjectArray JNICALL Java_ru_kfu_itis_issst_crfsuite4j_CrfSuiteTagger_doTag(JNIEnv *env, jobject obj, jobjectArray jItemArr){
	Tagger *tagger = getHandle<Tagger>(env, obj);
	ItemSequence itemSeq = toItemSequence(env, jItemArr);
	StringList labels = tagger->tag(itemSeq);
	// TODO handle C++ exceptions and send them to Java
	return toJavaStringArray(env, labels);
}
