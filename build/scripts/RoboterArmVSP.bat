@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem
@rem SPDX-License-Identifier: Apache-2.0
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  RoboterArmVSP startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem This is normally unused
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and ROBOTER_ARM_VSP_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS="-Dprism.verbose=true" "-Dprism.debug=true" "-Dprism.forceGPU=true" "-Djavafx.animation.fullspeed=true" "-XX:+UseG1GC" "-XX:+UseStringDeduplication" "-Dprism.order=es2,sw"

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH. 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME% 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\RoboterArmVSP.jar;%APP_HOME%\lib\CaDSControlGUI.jar;%APP_HOME%\lib\CaDSLogger.jar;%APP_HOME%\lib\CaDSRoboticArmInterface.jar;%APP_HOME%\lib\javafx-fxml-17.0.15-linux.jar;%APP_HOME%\lib\javafx-fxml-17.0.15.jar;%APP_HOME%\lib\javafx-controls-17.0.15-linux.jar;%APP_HOME%\lib\javafx-controls-17.0.15.jar;%APP_HOME%\lib\javafx-graphics-17.0.15-linux.jar;%APP_HOME%\lib\javafx-graphics-17.0.15.jar;%APP_HOME%\lib\javafx-base-17.0.15-linux.jar;%APP_HOME%\lib\javafx-base-17.0.15.jar;%APP_HOME%\lib\slf4j-simple-2.0.7.jar;%APP_HOME%\lib\slf4j-api-2.0.7.jar;%APP_HOME%\lib\gson-2.10.1.jar


@rem Execute RoboterArmVSP
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %ROBOTER_ARM_VSP_OPTS%  -classpath "%CLASSPATH%" cads.roboticArm.simulation.Server %*

:end
@rem End local scope for the variables with windows NT shell
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem Set variable ROBOTER_ARM_VSP_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%ROBOTER_ARM_VSP_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
