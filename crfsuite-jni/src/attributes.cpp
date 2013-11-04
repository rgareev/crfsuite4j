#include "commons.h"
#include "attributes.h"

ItemSequence toItemSequence(JNIEnv* env, jobjectArray jItemArr){
	jsize itemNum = env->GetArrayLength(jItemArr);
	ItemSequence itemSeq;
	for(int i=0; i<itemNum; i++) {
		Item item;
		jobject _attrSeqObj = env->GetObjectArrayElement(jItemArr, i);
		jobjectArray _attrSeq = static_cast<jobjectArray>(_attrSeqObj);
		jsize attrNum = env->GetArrayLength(_attrSeq);
		for(int a=0; a<attrNum; a++) {
			jobject jAttr = env->GetObjectArrayElement(_attrSeq, a);
			std::string attrName = getAttributeName(env, jAttr);
			Attribute attr(attrName);
			// delete attrName;
			item.push_back(attr);
		}
		itemSeq.push_back(item);
	}
	return itemSeq;
}

jobjectArray toJavaStringArray(JNIEnv* env, StringList& stringList){
	size_t s = stringList.size();
	jobjectArray result = newStringArray(env, s);
	for(size_t i=0; i<s; i++){
		jstring label = fromStdString(env, stringList[i]);
		env->SetObjectArrayElement(result, i, label);
	}
	return result;
}