if (( "$1" > 11 ));
then
    rm gradle/wrapper/gradle-wrapper.properties
    cp gradle/wrapper/gradle-wrapper.4.9.properties gradle/wrapper/gradle-wrapper.properties
elif (( "$1" < 12 )) && (( "$1" > 7 ))
then
    rm gradle/wrapper/gradle-wrapper.properties
    cp gradle/wrapper/gradle-wrapper.2.7.properties gradle/wrapper/gradle-wrapper.properties
fi