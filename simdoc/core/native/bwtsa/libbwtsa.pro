include(libbwtsa_options.pro)
include(libbwtsa_sources.pro)

TARGET    = bwtsa
CONFIG   += warn_on create_prl
TEMPLATE  = lib

QMAKE_CXXFLAGS += -fno-exceptions -fno-rtti -std=c++11

QMAKE_CXXFLAGS_DEBUG -= -g
QMAKE_CXXFLAGS_DEBUG += -g3
QMAKE_CXXFLAGS_RELEASE -= -O2
QMAKE_CXXFLAGS_RELEASE += -Ofast -march=native -flto
QMAKE_LFLAGS_RELEASE += -flto

debug {
    DEFINES += DEBUG
}
release {
}

QMAKE_INCDIR += \
                /usr/lib/jvm/java-7-openjdk-amd64/include/ \
                /usr/lib/jvm/java-7-openjdk-amd64/include/linux/ \
                /System/Library/Frameworks/JavaVM.framework/Versions/Current/Headers

