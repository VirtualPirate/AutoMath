#set(JNI_INCLUDE_DIR "F:/programs/java/include/")
#set(WIN32_JNI "F:/programs/java/include/win32/")

include_directories(${JNI_INCLUDE_DIR} ${WIN32_JNI} Adder)
add_library(jni_api
	Class.cpp
	Object.cpp
)