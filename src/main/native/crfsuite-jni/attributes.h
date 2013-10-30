#ifndef _ATTRIBUTES_H_INCLUDED
#define _ATTRIBUTES_H_INCLUDED

#include <jni.h>
#include <crfsuite_api.hpp>

using namespace CRFSuite;

ItemSequence toItemSequence(JNIEnv* env, jobjectArray jItemArr);

jobjectArray toJavaStringArray(JNIEnv* env, StringList& stringList);

#endif // _ATTRIBUTES_H_INCLUDED