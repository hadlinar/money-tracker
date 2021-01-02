////
//// Created by Hadlina Rahmadinni on 02/01/21.
////

#include <jni.h>

JNIEXPORT jdouble JNICALL
Java_id_ac_ui_cs_mobileprogramming_hadlina_moneytracker_utils_Calculator_add(JNIEnv *env, jobject thiz, jdouble a, jdouble b) {
    return a+b;
}

JNIEXPORT jdouble JNICALL
Java_id_ac_ui_cs_mobileprogramming_hadlina_moneytracker_utils_Calculator_subtract(JNIEnv *env, jobject thiz, jdouble a, jdouble b) {
    return a-b;
}

JNIEXPORT jdouble JNICALL
Java_id_ac_ui_cs_mobileprogramming_hadlina_moneytracker_utils_Calculator_multiply(JNIEnv *env, jobject thiz, jdouble a, jdouble b) {
    return a*b;
}

JNIEXPORT jdouble JNICALL
Java_id_ac_ui_cs_mobileprogramming_hadlina_moneytracker_utils_Calculator_divide(JNIEnv *env, jobject thiz, jdouble a, jdouble b) {
    return a/b;
}