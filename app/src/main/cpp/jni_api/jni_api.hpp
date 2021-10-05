#ifndef JNI_API_H
#define JNI_API_H

#include "jni.h"
#include <unordered_map>
#include <string>

jstring Create_Java_String(JNIEnv*, const std::string&);
std::string jstringtostring(JNIEnv*, jstring);

class Class{
public:
	static JNIEnv* environment;
	jclass class_;
	std::unordered_map<std::string, jfieldID> instance_fields;
	std::unordered_map<std::string, jfieldID> static_fields;

	Class();
	Class(const char*);
	Class(const jclass&);
	Class& operator=(const jclass&);
	Class(const jobject&);

	void setInstanceField(const char*, const char*);

	void setStaticField(const char*, const char*);

	jfieldID GetFieldID(const std::string&);

	jfieldID GetStaticFieldID(const std::string&);

	static void setEnvironment(JNIEnv*);
};

class Object{
	static JNIEnv* environment;

	jobject object;
	Class class_;

public:
	static void setEnvironment(JNIEnv*);

	Object(const jobject&);
	Object(const jobject&, const Class&);
	operator bool() const;
	void setObject(const jobject&);
	jobject getObject(const jobject&);
	void setClass(const jclass&);
	Class getClass();

	Object GetField(const std::string&);
	jobject GetObjectField(const std::string&);
	jboolean GetBooleanField(const std::string&);
	jbyte GetByteField(const std::string&);
	jchar GetCharField(const std::string&);
	jshort GetShortField(const std::string&);
	jint GetIntField(const std::string&);
	jlong GetLongField(const std::string&);
	jfloat GetFloatField(const std::string&);
	jdouble GetDoubleField(const std::string&);

	jobject GetStaticObjectField(const std::string&);
	jboolean GetStaticBooleanField(const std::string&);
	jbyte GetStaticByteField(const std::string&);
	jchar GetStaticCharField(const std::string&);
	jshort GetStaticShortField(const std::string&);
	jint GetStaticIntField(const std::string&);
	jlong GetStaticLongField(const std::string&);
	jfloat GetStaticFloatField(const std::string&);
	jdouble GetStaticDoubleField(const std::string&);


};

#endif