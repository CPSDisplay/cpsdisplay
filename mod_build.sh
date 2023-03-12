# Just cleaning
BIN_DIR="bin"
BUILD_DIR="build"

if [ -d "$BIN_DIR" ];
then
    rm -rf bin/
fi

if [ -d "$BUILD_DIR" ];
then
    rm -rf build/"!(libs)"
fi

./gradlew --build-file build.$1.gradle build