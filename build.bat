@echo off
REM This is for a future build system.
SET arg1=%1
SET arg2=%2
SET arg3=%3
SET arg4=%4
SET arg5=%5
SET arg6=%6

SET BUILD_FOR_ANDROID=1
SET INSTALL_ON_PHONE=0
SET RUN_AVD= 0
SET BUILD_FOR_PC=1
SET DEGUG=1

if %BUILD_FOR_ANDROID% == 1 (
   if %DEGUG% == 1 (
      py -3 switch_build.py "Android"
      ant debug
      py -3 switch_build.py "PC" ))

if %BUILD_FOR_ANDROID% == 1 (
   if %DEGUG% == 0 (
      py -3 switch_build.py "Android"
      ant release
      py -3 switch_build.py "PC" ))


SET CODE=com/urquieta/something
SET SRC=src/%CODE%

if %BUILD_FOR_PC% == 1 (
   if %DEGUG% == 1 (
     javac -g -Xdiags:verbose -Xlint:all -d bin %SRC%/PCMain.java %SRC%/game/*.java %SRC%/game/board/*.java %SRC%/platform/*.java %SRC%/platform/pc/*.java %SRC%/game/util/*.java
     pushd bin
     jar -cvmf ..\MANIFEST.MF Something.jar %CODE%/*.class %CODE%/game/*.class %CODE%/platform/*.class %CODE%/platform/pc/*.class %CODE%/game/board/*.class %CODE%/game/util/*.class
     popd ))
