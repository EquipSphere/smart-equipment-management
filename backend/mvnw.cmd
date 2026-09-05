@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM ----------------------------------------------------------------------------

@IF "%DEBUG%" == "" @ECHO OFF
@REM set %ENABLE_DELAYED_EXPANSION% to on if you need it
IF "%OS%"=="Windows_NT" SETLOCAL enableextensions

SET ERROR_CODE=0

@REM set maven wrapper path
SET MAVEN_PROJECTBASEDIR=%~dp0
IF NOT "%MAVEN_PROJECTBASEDIR%"=="" SET MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR:~0,-1%

SET WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
SET WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

@REM Extension to allow configuring maven toolchain, etc.
IF EXIST "%MAVEN_PROJECTBASEDIR%\.mvn\jvm.config" (
    FOR /F "usebackq delims=" %%A in ("%MAVEN_PROJECTBASEDIR%\.mvn\jvm.config") do set JVM_CONFIG_MAVEN_PROPS=!JVM_CONFIG_MAVEN_PROPS! %%A
)

SET MAVEN_JAVA_EXE="%JAVA_HOME%\bin\java.exe"
IF NOT EXIST %MAVEN_JAVA_EXE% (
    SET MAVEN_JAVA_EXE=java.exe
)

%MAVEN_JAVA_EXE% -version >nul 2>&1
IF ERRORLEVEL 1 (
    echo Error: JAVA_HOME is not defined correctly.
    echo We cannot execute java.exe
    goto error
)

%MAVEN_JAVA_EXE% %JVM_CONFIG_MAVEN_PROPS% %MAVEN_OPTS% -classpath %WRAPPER_JAR% "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" %WRAPPER_LAUNCHER% %*
if ERRORLEVEL 1 goto error
goto end

:error
SET ERROR_CODE=1

:end
@IF "%ERROR_CODE%"=="0" (
  EXIT /B 0
) ELSE (
  EXIT /B 1
)
