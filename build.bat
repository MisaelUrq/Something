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
SET BUILD_FOR_PC=0
SET DEGUG=1

if %BUILD_FOR_ANDROID% == 1 (
   if %DEGUG% == 1 (
      ant debug ))

if %BUILD_FOR_ANDROID% == 1 (
   if %DEGUG% == 0 (
      ant release ))
