LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := JniUtil
LOCAL_SRC_FILES =: JniUtil.cpp
include $(BUILD_SHARED_LIBRARY)