#include "net_kamfat_omengo_util_JniUtil.h"

JNIEXPORT jstring JNICALL Java_net_kamfat_omengo_util_JniUtil_test
        (JNIEnv *env, jobject obj)
{
    return (*env).NewStringUTF("this is test");
}