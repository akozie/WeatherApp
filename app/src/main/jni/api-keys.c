#include <jni.h>



JNIEXPORT jstring JNICALL
Java_com_infinion_infinion_utils_UtilityParam_getApiKey(JNIEnv *env, jobject thiz) {
    return (*env) -> NewStringUTF(env, "12c96e287a5d4f23f3ec2f320de66005");
}

JNIEXPORT jstring JNICALL
Java_com_infinion_infinion_utils_UtilityParam_getBaseUrl(JNIEnv *env, jobject thiz) {
    return (*env) -> NewStringUTF(env, "https://api.openweathermap.org/data/2.5/");
}