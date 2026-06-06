@REM Maven Wrapper startup batch script
@IF "%DEBUG%"=="" @echo off
@setlocal

set "MAVEN_PROJECTBASEDIR=%~dp0"
set "WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar"

@REM Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome
set "JAVA_EXE=java"
goto execute

:findJavaFromJavaHome
set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
if not exist "%JAVA_EXE%" (
    echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
    exit /b 1
)

:execute
"%JAVA_EXE%" %MAVEN_OPTS% "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" -classpath "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
if %ERRORLEVEL% neq 0 goto error
goto end

:error
exit /b %ERRORLEVEL%

:end
@endlocal
