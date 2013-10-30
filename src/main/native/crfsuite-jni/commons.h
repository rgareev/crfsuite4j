#ifndef _COMMONS_H_INCLUDED_
#define _COMMONS_H_INCLUDED_

#include <jni.h>
#include <string>

// TODO insert assertions that sizeof(jlong) >= sizeof(void*)

jfieldID getHandleField(JNIEnv *env, jobject obj);

template <typename T> T *getHandle(JNIEnv *env, jobject obj)
{
	jlong handle = env->GetLongField(obj, getHandleField(env, obj));
	return reinterpret_cast<T *>(handle);
}

template <typename T> void setHandle(JNIEnv *env, jobject obj, T *t)
{
	jlong handle = reinterpret_cast<jlong>(t);
	env->SetLongField(obj, getHandleField(env, obj), handle);
}

std::string toStdString(JNIEnv *env, jstring jstr);

jstring fromStdString(JNIEnv *env, const std::string &str);

std::string getAttributeName(JNIEnv *env, jobject obj);

void callTrainerMessageMethod(JNIEnv *env, jobject obj, jstring jmsg);

jobjectArray newStringArray(JNIEnv *env, size_t size);

#endif // _COMMONS_H_INCLUDED_