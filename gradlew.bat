@rem Gradle wrapper baslatici (Windows)
@if "%DEBUG%"=="" @echo off
setlocal
set DIRNAME=%~dp0
set APP_HOME=%DIRNAME%
set CLASSPATH=%APP_HOME%gradle\wrapper\gradle-wrapper.jar
if not exist "%CLASSPATH%" (
  echo HATA: gradle\wrapper\gradle-wrapper.jar bulunamadi. 1>&2
  exit /b 1
)
if defined JAVA_HOME ( set JAVA_EXE=%JAVA_HOME%\bin\java.exe ) else ( set JAVA_EXE=java.exe )
"%JAVA_EXE%" -Xmx64m -Xms64m %JAVA_OPTS% %GRADLE_OPTS% "-Dorg.gradle.appname=gradlew" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
endlocal
