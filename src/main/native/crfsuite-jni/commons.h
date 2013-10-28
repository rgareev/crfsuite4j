#ifndef _COMMONS_H_INCLUDED_
#define _COMMONS_H_INCLUDED_

#include <jni.h>

// TODO insert assertions that sizeof(jlong) >= sizeof(void*)

jfieldID getHandleField(JNIEnv *env, jobject obj)
{
	jclass c = env->GetObjectClass(obj);
	// J is the type signature for long:
	return env->GetFieldID(c, "nativeHandle", "J");
}

template <typename T>
T *getHandle(JNIEnv *env, jobject obj)
{
	jlong handle = env->GetLongField(obj, getHandleField(env, obj));
	return reinterpret_cast<T *>(handle);
}

template <typename T>
void setHandle(JNIEnv *env, jobject obj, T *t)
{
	jlong handle = reinterpret_cast<jlong>(t);
	env->SetLongField(obj, getHandleField(env, obj), handle);
}

std::string toStdString(JNIEnv *env, jstring jstr){
	const char *cChars = env->GetStringUTFChars(jstr, NULL);
	std::string result(cChars);
	env->ReleaseStringUTFChars(jstr, cChars);
	return result;
}

jstring fromStdString(JNIEnv *env, const std::string &str){
	const char *cStr = str.c_str();
	return env->NewStringUTF(cStr);
}

std::string getAttributeName(JNIEnv *env, jobject obj) {
	jclass attrClass = env->FindClass("ru/kfu/itis/issst/crfsuite4j/Attribute");
	jmethodID attrGetName = env->GetMethodID(attrClass, "getName", "()Ljava/lang/String;");
	jstring jAttrName = static_cast<jstring>(env->CallObjectMethod(obj, attrGetName));
	return toStdString(env, jAttrName);
}

void callTrainerMessageMethod(JNIEnv *env, jobject obj, jstring jmsg){
	jclass trClass = env->FindClass("ru/kfu/itis/issst/crfsuite4j/CrfSuiteTrainer");
	jmethodID messageMethod = env->GetMethodID(trClass, "message", "(Ljava/lang/String;)V");
	env->CallVoidMethod(obj, messageMethod, jmsg);
	// TODO handle exceptions
}

jobjectArray newStringArray(JNIEnv *env, int size) {
	jclass stringClass = env->FindClass("java/lang/String");
	return env->NewObjectArray(size, stringClass, NULL);
}

#endif