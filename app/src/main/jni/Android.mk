APP_PLATFORM:=android-22
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_C_INCLUDES := $(LOCAL_PATH)/librtmp
LOCAL_ALLOW_UNDEFINED_SYMBOLS := true
LOCAL_LDLIBS :=-llog
LOCAL_MODULE := publish_jni
LOCAL_SRC_FILES :=  \
./publish_jni.cpp \
./librtmp/rtmp.c \
./librtmp/amf.c \
./librtmp/hashswf.c \
./librtmp/log.c \
./librtmp/parseurl.c \
./Rtmp.cpp
include $(BUILD_SHARED_LIBRARY)
LOCAL_LDFLAGS += -fuse-ld=bfd