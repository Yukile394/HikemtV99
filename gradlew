#!/bin/sh
# Gradle wrapper baslatici (POSIX sh)
app_path=$0
while [ -h "$app_path" ]; do
    ls=$(ls -ld -- "$app_path"); link=${ls#*' -> '}
    case $link in /*) app_path=$link ;; *) app_path=${app_path%/*}/$link ;; esac
done
APP_HOME=$(cd "${app_path%/*}" > /dev/null && pwd -P) || exit
CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar
if [ ! -f "$CLASSPATH" ]; then
    echo "HATA: gradle/wrapper/gradle-wrapper.jar bulunamadi. 'gradle wrapper' calistirin veya GitHub Actions workflow'unu kullanin." >&2
    exit 1
fi
if [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/java" ]; then JAVACMD=$JAVA_HOME/bin/java; else JAVACMD=java; fi
exec "$JAVACMD" -Xmx64m -Xms64m $JAVA_OPTS $GRADLE_OPTS \
    "-Dorg.gradle.appname=gradlew" -classpath "$CLASSPATH" \
    org.gradle.wrapper.GradleWrapperMain "$@"
