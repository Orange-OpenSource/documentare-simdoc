#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "com_orange_documentare_core_comp_bwt_SaisBwt.h"
#include "divsufsort.h"

#define LIB_VERSION 3

JNIEXPORT jbyteArray JNICALL Java_com_orange_documentare_core_comp_bwt_SaisBwt_getBwtNative
  (JNIEnv * env, jobject, jbyteArray jbytes) {

  const int bytesLen = env->GetArrayLength(jbytes);
  unsigned char * inputBytes = (unsigned char*)env->GetPrimitiveArrayCritical(jbytes, 0);

  const int doubleLen = bytesLen * 2;
  unsigned char * doubleBytesInput;
  int * suffixArrayIndices;

  // Allocate 5blocksize bytes of memory
  doubleBytesInput = (unsigned char*)malloc(doubleLen);
  suffixArrayIndices = (int *)malloc(doubleLen * sizeof(int));
  if((doubleBytesInput == NULL) || (suffixArrayIndices == NULL)) {
    fprintf(stderr, "getNative: Cannot allocate arrays (%d bytes)\n", doubleLen);
    env->ReleaseByteArrayElements(jbytes, (jbyte*)inputBytes, JNI_ABORT);
    return NULL;
  }

  // For SA BWT, we need to double the input array
  memcpy(doubleBytesInput, inputBytes, bytesLen);
  memcpy(doubleBytesInput + bytesLen, inputBytes, bytesLen);

  // Create sorted suffix array
  if(divsufsort(doubleBytesInput, suffixArrayIndices, doubleLen) != 0) {
    fprintf(stderr, "getNative: Sort failed due to OOM.");
    env->ReleasePrimitiveArrayCritical(jbytes, (jbyte*)inputBytes, JNI_ABORT);
    return NULL;
  }
  free(doubleBytesInput);

  // Retrieve output from suffix array
  unsigned char * const outputBytes = (unsigned char*)malloc(bytesLen);
  int outputIndex = 0;
  int saIndex;
  int origIndex;
  for (int i = 0; i < doubleLen; i++) {
    saIndex = suffixArrayIndices[i];
    if (saIndex < bytesLen) {
      origIndex = saIndex - 1;
      origIndex = origIndex < 0 ? bytesLen - 1 : origIndex;
      outputBytes[outputIndex++] = inputBytes[origIndex];
    }
  }

  free(suffixArrayIndices);
  // Prep returned jbyteArray
  jbyteArray result = env->NewByteArray(bytesLen);

  env->ReleasePrimitiveArrayCritical(jbytes, (jbyte*)inputBytes, JNI_ABORT);

  if (result == NULL) {
    fprintf(stderr, "getNative: Cannot allocate output array\n");
    return NULL;
  } else {
    env->SetByteArrayRegion(result, 0, bytesLen, (jbyte*)outputBytes);
    return result;
  }
}


/******** JNI boilerplate code ********/

void register_Natives(JavaVM* vm) {
  JNIEnv * env;
  vm->GetEnv((void **)&env, JNI_VERSION_1_6);
  jclass javaClass = env->FindClass("com/orange/documentare/core/comp/bwt/SaisBwt");
  JNINativeMethod methods [] = {
    { (char*)"getBwtNative", (char*)"([B)[B", (void*) & Java_com_orange_documentare_core_comp_bwt_SaisBwt_getBwtNative }
  };
  env->RegisterNatives(javaClass, methods, 1);
}

jint JNI_OnLoad(JavaVM* vm, void*) {
  fprintf(stdout, "Native lib version: %d\n", LIB_VERSION);
  register_Natives(vm);
  return JNI_VERSION_1_6;
}
