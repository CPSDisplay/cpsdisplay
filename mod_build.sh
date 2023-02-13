# Just cleaning
rm -rf bin/
rm -rf build/

./gradlew --build-file build.$1.gradle build