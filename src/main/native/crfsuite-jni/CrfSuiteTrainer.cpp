#include <crfsuite_api.hpp>
#include "commons.h"
#include "attributes.h"
#include "ru_kfu_itis_issst_crfsuite4j_CrfSuiteTrainer.h"

using namespace CRFSuite;

class TrainerWithJavaLoggingCallback : public Trainer {
private:
	JavaVM* const jvm;
	jobject const jTrainer;
public:
	TrainerWithJavaLoggingCallback(JavaVM* _jvm, jobject _jTrainer) : jvm(_jvm), jTrainer(_jTrainer) {
	}

	void message(const std::string& msg){
		JNIEnv *env;
		jint getEnvStat = jvm->GetEnv((void**)&env, JNI_VERSION_1_6);
		bool attached = false;
		if(getEnvStat == JNI_EDETACHED) {
			printf("GetEnv: not attached\n");
			if(jvm->AttachCurrentThread((void**)&env, NULL) != JNI_OK) {
				// TODO raise java exception
				printf("Failed to attach JNIEnv to JavaVM\n");
				return;
			}
			attached = true;
		} else if(getEnvStat == JNI_OK) {
			// OK
		} else if(getEnvStat = JNI_EVERSION) {
			printf("GetEnv: JNI version 1.6 is not supported\n");
			return;
		}
		callTrainerMessageMethod(env, jTrainer, fromStdString(env, msg));
		if(attached){
			jvm->DetachCurrentThread();
		}
	}

	void deleteJTrainerRef(JNIEnv *env){
		env->DeleteGlobalRef(jTrainer);
	}
};

JNIEXPORT void JNICALL Java_ru_kfu_itis_issst_crfsuite4j_CrfSuiteTrainer_init(JNIEnv *env, jobject localObj) {
	JavaVM *jvm;
	env->GetJavaVM(&jvm);
	jobject glObj = env->NewGlobalRef(localObj);
	Trainer *tr = new TrainerWithJavaLoggingCallback(jvm, glObj);
	setHandle(env, localObj, tr);
}

JNIEXPORT void JNICALL Java_ru_kfu_itis_issst_crfsuite4j_CrfSuiteTrainer_dispose(JNIEnv *env, jobject obj) {
	TrainerWithJavaLoggingCallback *tr = getHandle<TrainerWithJavaLoggingCallback>(env, obj);
	setHandle<jlong>(env, obj, 0);
	tr->deleteJTrainerRef(env);
	delete tr;
}

JNIEXPORT void JNICALL Java_ru_kfu_itis_issst_crfsuite4j_CrfSuiteTrainer_append(JNIEnv *env, jobject obj, jobjectArray _itemSeq, jobjectArray _labelSeq, jint group){
	Trainer *tr = getHandle<Trainer>(env, obj);
	// convert _itemSeq
	ItemSequence itemSeq = toItemSequence(env, _itemSeq);
	// convert _labelSeq
	jsize labelNum = env->GetArrayLength(_labelSeq);
	StringList labelSeq;
	for(int i=0; i<labelNum; i++){
		jstring jLabel = static_cast<jstring>(env->GetObjectArrayElement(_labelSeq, i));
		std::string label = toStdString(env, jLabel);
		labelSeq.push_back(label);
	}
	// invoke native delegate
	tr->append(itemSeq, labelSeq, group);
	// TODO handle exceptions
}

JNIEXPORT jboolean JNICALL Java_ru_kfu_itis_issst_crfsuite4j_CrfSuiteTrainer_doSelect(JNIEnv *env, jobject obj, jstring jAlg, jstring jType){
	Trainer *tr = getHandle<Trainer>(env, obj);
	std::string alg = toStdString(env, jAlg);
	std::string modelType = toStdString(env, jType);
	return tr->select(alg, modelType) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jint JNICALL Java_ru_kfu_itis_issst_crfsuite4j_CrfSuiteTrainer_doTrain(JNIEnv *env, jobject obj, jstring jModelFile, jint holdout){
	Trainer *tr = getHandle<Trainer>(env, obj);
	std::string modelFile = toStdString(env, jModelFile);
	return tr->train(modelFile, holdout);
}

JNIEXPORT jobjectArray JNICALL Java_ru_kfu_itis_issst_crfsuite4j_CrfSuiteTrainer_params(JNIEnv *env, jobject obj){
	Trainer *tr = getHandle<Trainer>(env, obj);
	StringList params = tr->params();
	jobjectArray jResult = newStringArray(env, params.size());
	for(size_t i=0; i<params.size(); i++){
		std::string param = params[i];
		jstring jParam = fromStdString(env, param);
		env->SetObjectArrayElement(jResult, i, jParam);
	}
	return jResult;
}

JNIEXPORT void JNICALL Java_ru_kfu_itis_issst_crfsuite4j_CrfSuiteTrainer_set(JNIEnv *env, jobject obj, jstring jName, jstring jVal){
	Trainer *tr = getHandle<Trainer>(env, obj);
	std::string name = toStdString(env, jName);
	std::string val = toStdString(env, jVal);
	tr->set(name, val);
	// TODO handle exception
}

JNIEXPORT jstring JNICALL Java_ru_kfu_itis_issst_crfsuite4j_CrfSuiteTrainer_get(JNIEnv *env, jobject obj, jstring jName){
	Trainer *tr = getHandle<Trainer>(env, obj);
	std::string name = toStdString(env, jName);
	std::string val = tr->get(name);
	// TODO handle exception
	return fromStdString(env, val);
}

JNIEXPORT jstring JNICALL Java_ru_kfu_itis_issst_crfsuite4j_CrfSuiteTrainer_help(JNIEnv *env, jobject obj, jstring jName){
	Trainer *tr = getHandle<Trainer>(env, obj);
	std::string name = toStdString(env, jName);
	std::string help = tr->help(name);
	return fromStdString(env, help);
}
