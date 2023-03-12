# Just cleaning
rm -rf bin/
rm -rf build/!("libs")

./gradlew --build-file build.$1.gradle build