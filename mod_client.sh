# ex: ./launch_client.sh 8
./mod_gradle.sh $1
./gradlew --build-file build.$1.gradle runClient