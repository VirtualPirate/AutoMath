#include "Substitutor.hpp"
#include "Operand.hpp"
#include "Constant.hpp"
#include "Variable.hpp"
#include "OperandList.hpp"
#include "Term.hpp"
#include "Expression.hpp"
#include "Operation_Functions.hpp"
#include "Parser.hpp"

#include "Tests.hpp"
#include "jni.h"

#include "jni_api/jni_api.hpp"
#include "ExpressionStream.h"

#include <string>

jstring Create_Java_String(JNIEnv *environment, const std::string& mystr){
	return environment->NewStringUTF(mystr.data());
}

std::string jstringtostring(JNIEnv *env, jstring jStr) {
    if(!jStr)
        return "";

       const jclass stringClass = env->GetObjectClass(jStr);
       const jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
       const jbyteArray stringJbytes = (jbyteArray) env->CallObjectMethod(jStr, getBytes, env->NewStringUTF("UTF-8"));

       size_t length = (size_t) env->GetArrayLength(stringJbytes);
       jbyte* pBytes = env->GetByteArrayElements(stringJbytes, NULL);

       std::string ret = std::string((char *)pBytes, length);
       env->ReleaseByteArrayElements(stringJbytes, pBytes, JNI_ABORT);

       env->DeleteLocalRef(stringJbytes);
       env->DeleteLocalRef(stringClass);
       return ret;
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_phantom_automath_ExpressionStream_invoke_1simplify_1stream(JNIEnv *env, jobject object){

		Object::setEnvironment(env);
		Class::setEnvironment(env);

        Class subtitute_class{"com/phantom/automath/Subtitute"};
		subtitute_class.setInstanceField("name", "C");
		subtitute_class.setInstanceField("value", "D");

		Class expressionstream_class{"com/phantom/automath/ExpressionStream"};
		expressionstream_class.setInstanceField("subtitute_list", "[Lcom/phantom/automath/Subtitute;");
		expressionstream_class.setInstanceField("expression", "Ljava/lang/String;");

		Object expressionstream_object{object, expressionstream_class};
		jobject expression_object = expressionstream_object.GetObjectField("expression");
		std::string expression = jstringtostring(env, (jstring)expression_object);

        jobject subtitute_list_object = expressionstream_object.GetObjectField("subtitute_list");
		jobjectArray* objectArray = reinterpret_cast<jobjectArray*>(&subtitute_list_object);

		// //Get object array lenght;
		jarray array = *objectArray;
		jsize array_size = env->GetArrayLength(array);
		//std::cout << "subtitute_list lenght = " << env->GetArrayLength(array) << std::endl;
        Variable_Subtitutor_List subtitute_list;

		jobject each_subtitute;
		for(int index=0; index < array_size; index++){
			each_subtitute = env->GetObjectArrayElement(*objectArray, index);
			Object each_object{each_subtitute, subtitute_class};
			jchar name = each_object.GetCharField("name");
			jdouble value = each_object.GetDoubleField("value");
            subtitute_list.append(Variable_Subtitutor((char)name, (double)value));
		}

        Operand operand{expression};
        if(operand){
            if(operand.is_expression()){
                Expression exp{operand.get<Expression>()};
                std::ostringstream stream_;
		        exp.subtitute(subtitute_list).get<Expression>().simplify(stream_);
                return Create_Java_String(env, stream_.str());
            }
            else{
                Operand result{operand.simplify()};
                std::ostringstream stream_;
                stream_ << result;
                return Create_Java_String(env, stream_.str());
            }
        }
        return Create_Java_String(env, "The expression is invalid ...");

    }

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_phantom_automath_ExpressionStream_00024Companion_isValidExpression(JNIEnv *env,
                                                                            jobject thiz,
                                                                            jstring expression) {
    // TODO: implement isValidExpression()
    std::string expression_ = jstringtostring(env, expression);
    if(Operand{expression_})
        return true;
    return false;
}