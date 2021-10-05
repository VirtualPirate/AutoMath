#include <cstdio>
#include <iostream>
#include <unordered_map>
#include "jni_api.hpp"

JNIEnv* Object::environment = nullptr;
void Object::setEnvironment(JNIEnv* env){
	Object::environment = env;
}

Object::Object(const jobject& object_): object{object_}, class_{Object::environment->GetObjectClass(object_)}{}
Object::Object(const jobject& object_, const Class& cls){
	if(environment->IsInstanceOf(object_, cls.class_)){
		object = object_;
		class_ = cls;
	}
}
Object::operator bool() const {
	return environment->IsInstanceOf(object, class_.class_);
}
void Object::setObject(const jobject& object_){
	object = object_;
}
jobject Object::getObject(const jobject& object_){
	return object;
}
void Object::setClass(const jclass& cls){
	class_ = cls;
}
Class Object::getClass(){
	return class_;
}

Object Object::GetField(const std::string& field_name){return Object::environment->GetObjectField(object, class_.instance_fields.at(field_name));}
jobject Object::GetObjectField(const std::string& field_name){return Object::environment->GetObjectField(object, class_.instance_fields.at(field_name));}
jboolean Object::GetBooleanField(const std::string& field_name){return Object::environment->GetBooleanField(object, class_.instance_fields.at(field_name));}
jbyte Object::GetByteField(const std::string& field_name){return Object::environment->GetByteField(object, class_.instance_fields.at(field_name));}
jchar Object::GetCharField(const std::string& field_name){return Object::environment->GetCharField(object, class_.instance_fields.at(field_name));}
jshort Object::GetShortField(const std::string& field_name){return Object::environment->GetShortField(object, class_.instance_fields.at(field_name));}
jint Object::GetIntField(const std::string& field_name){return Object::environment->GetIntField(object, class_.instance_fields.at(field_name));}
jlong Object::GetLongField(const std::string& field_name){return Object::environment->GetLongField(object, class_.instance_fields.at(field_name));}
jfloat Object::GetFloatField(const std::string& field_name){return Object::environment->GetFloatField(object, class_.instance_fields.at(field_name));}
jdouble Object::GetDoubleField(const std::string& field_name){return Object::environment->GetDoubleField(object, class_.instance_fields.at(field_name));}

jobject Object::GetStaticObjectField(const std::string& field_name){return Object::environment->GetStaticObjectField(class_.class_, class_.static_fields.at(field_name));}
jboolean Object::GetStaticBooleanField(const std::string& field_name){return Object::environment->GetStaticBooleanField(class_.class_, class_.static_fields.at(field_name));}
jbyte Object::GetStaticByteField(const std::string& field_name){return Object::environment->GetStaticByteField(class_.class_, class_.static_fields.at(field_name));}
jchar Object::GetStaticCharField(const std::string& field_name){return Object::environment->GetStaticCharField(class_.class_, class_.static_fields.at(field_name));}
jshort Object::GetStaticShortField(const std::string& field_name){return Object::environment->GetStaticShortField(class_.class_, class_.static_fields.at(field_name));}
jint Object::GetStaticIntField(const std::string& field_name){return Object::environment->GetStaticIntField(class_.class_, class_.static_fields.at(field_name));}
jlong Object::GetStaticLongField(const std::string& field_name){return Object::environment->GetStaticLongField(class_.class_, class_.static_fields.at(field_name));}
jfloat Object::GetStaticFloatField(const std::string& field_name){return Object::environment->GetStaticFloatField(class_.class_, class_.static_fields.at(field_name));}
jdouble Object::GetStaticDoubleField(const std::string& field_name){return Object::environment->GetStaticDoubleField(class_.class_, class_.static_fields.at(field_name));}
