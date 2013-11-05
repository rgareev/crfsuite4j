CC := g++
SRCDIR := src
BUILD_DIR := build
TARGET_SONAME := libcrfsuite-jni.so
TARGET := bin/${TARGET_SONAME}
CPPFLAGS := -Wall -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux
CXXFLAGS := -fPIC -O3
SOURCES := $(wildcard $(SRCDIR)/*.cpp)
OBJECTS := $(patsubst $(SRCDIR)/%,$(BUILD_DIR)/%,$(SOURCES:.cpp=.o))
HEADERS := $(wildcard $(SRCDIR)/*.h)

${TARGET} : ${OBJECTS}
	mkdir -p bin
	${CC} -shared ${CXXFLAGS} -Wl,-soname,${TARGET_SONAME} -o ${TARGET} ${OBJECTS} -l:libcrfsuite.a -l:libcqdb.a -l:liblbfgs.a
# ${OBJECTS} : ${HEADERS}
${BUILD_DIR}/%.o : ${SRCDIR}/%.cpp ${HEADERS}
	mkdir -p ${BUILD_DIR}
	${CC} ${CPPFLAGS} ${CXXFLAGS} -c -o $@ $<
#
.PHONY : clean
clean :
	$(RM) -r ${BUILD_DIR} ${TARGET}

