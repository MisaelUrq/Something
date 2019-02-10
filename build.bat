@echo off

SET BUILD_FOR_ANDROID=1
SET INSTALL_ON_PHONE=0
SET RUN_AVD= 0
SET BUILD_FOR_PC=1
SET DEGUG=1

SET APP_NAME=Something
SET CODE=com/urquieta/something
SET SRC=src/%CODE%
SET ANDROID_CODE=%SRC%/MainActivity.java %SRC%/game/*.java %SRC%/game/board/*.java %SRC%/platform/*.java %SRC%/platform/android/*.java %SRC%/game/util/*.java
SET PC_CODE=%SRC%/PCMain.java %SRC%/game/*.java %SRC%/game/board/*.java %SRC%/platform/*.java %SRC%/platform/pc/*.java %SRC%/game/util/*.java %SRC%/game/ui/*.java %SRC%/output/*.java %SRC%/game/save/*.java
SET FILES_CLASSES=%CODE%/*.class %CODE%/game/*.class %CODE%/platform/*.class %CODE%/platform/pc/*.class %CODE%/game/board/*.class %CODE%/game/util/*.class %CODE%/game/ui/*.class %CODE%/output/*.class %CODE%/game/save/*.class

rem TODO(Misael): Make the build without ant.
rem SET AAPT="C:\Android\android-sdk\build-tools\28.0.3\aapt.exe"
rem SET DX="C:\Android\android-sdk\build-tools\28.0.3\dx.bat"
rem SET PLATFORM_ANDROID="C:\Android\android-sdk\platforms\android-16\android.jar"
rem SET ZIPALIGN="C:\Android\android-sdk\build-tools\28.0.3\zipalign.exe"
rem SET APKSIGNER="C:\Android\android-sdk\build-tools\28.0.3\apksigner.bat"

rem py -3 switch_build.py "Android"
rem echo "Generating R.java file"
rem %AAPT% package -f -m -J "gen" -M AndroidManifest.xml -A "assets" -S "res" -I %PLATFORM_ANDROID% -F "bin\resources.ap_"
rem javac -d "obj_android" -classpath "src" -bootclasspath %PLATFORM_ANDROID% -source 1.7 -target 1.7 %ANDROID_CODE%
rem javac -d "obj_android" -classpath "src" -bootclasspath %PLATFORM_ANDROID% -source 1.7 -target 1.7 "gen/com/urquieta/something/R.java"
rem call %DX% --dex --output="bin/classes.dex" "obj_android"
rem %AAPT% pacakge -f -m -F "bin\%APP_NAME%.unaligned.apk" -M AndroidManifest.xml -S "res" -A "assets" -I %PLATFORM_ANDROID%
rem %AAPT% add "bin\%APP_NAME%.unaligned.apk" "bin\classes.dex"
rem call %APKSIGNER% sign --ks key.keystore "bin\%APP_NAME%.unaligned.apk"
rem %ZIPALIGN% -f 4 "bin\%APP_NAME%.unaligned.apk" "bin\%APP_NAME%.apk"
rem %py -3 switch_build.py "PC"

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

if %BUILD_FOR_PC% == 1 (
   if %DEGUG% == 1 (
     javac -g -Xdiags:verbose -Xlint:all -d bin %PC_CODE%
     pushd bin
     jar -cvmf ..\MANIFEST.MF Something.jar %FILES_CLASSES%
     popd ))
