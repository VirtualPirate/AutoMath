#include <cstdio>
#include <iostream>
#include <unordered_map>
#include "jni_api.hpp"

JNIEnv* Class::environment = nullptr;
Class::Class(): class_{}, instance_fields{}{}
Class::Class(const char* class_sig): class_{environment->FindClass(class_sig)}, instance_fields{}{}
Class::Class(const jclass& cls): class_{cls}, instance_fields{}{}
Class& Class::operator=(const jclass& cls){
	class_ = cls;
	return *this;
}
Class::Class(const jobject& object): class_{Class::environment->GetObjectClass(object)}{}

void Class::setInstanceField(const char* field_name, const char* field_signature){
	jfieldID field_id = Class::environment->GetFieldID(class_, field_name, field_signature);
	if(field_id != NULL)
		instance_fields.emplace(field_name, field_id);
	else
		std::cerr << "[ERROR] - [FIELD] " << field_name << " with [FIELD_SIGNATURE] " << field_signature << " IS NOT FOUND\n";
}

void Class::setStaticField(const char* field_name, const char* field_signature){
	jfieldID field_id = Class::environment->GetStaticFieldID(class_, field_name, field_signature);
	if(field_id != NULL)
		static_fields.emplace(field_name, field_id);
	else
		std::cerr << "[ERROR] - [STATIC_FIELD] " << field_name << " with [FIELD_SIGNATURE] " << field_signature << " IS NOT FOUND\n";
}

jfieldID Class::GetFieldID(const std::string& field_name){
	return instance_fields.at(field_name);
}

jfieldID Class::GetStaticFieldID(const std::string& field_name){
	return static_fields.at(field_name);
}

void Class::setEnvironment(JNIEnv* env){
	Class::environment = env;
}