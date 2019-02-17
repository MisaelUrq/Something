@echo off

rm -rd obj_android/*


SET AAPT="C:\Android\android-sdk\build-tools\28.0.3\aapt.exe"
SET DX="C:\Android\android-sdk\build-tools\28.0.3\dx.bat"
SET PLATFORM_ANDROID="C:\Android\android-sdk\platforms\android-16\android.jar"
SET ZIPALIGN="C:\Android\android-sdk\build-tools\28.0.3\zipalign.exe"
SET APKSIGNER="C:\Android\android-sdk\build-tools\26.0.2\apksigner.bat"

SET BUILD_FOR_ANDROID=1
SET INSTALL_ON_PHONE=0
SET RUN_AVD= 0
SET BUILD_FOR_PC=0
SET DEGUG=1

SET APP_NAME=Something
SET CODE=com/urquieta/something
SET SRC=src/%CODE%
SET ANDROID_CODE=%SRC%/MainActivity.java %SRC%/game/*.java %SRC%/game/board/*.java %SRC%/platform/*.java %SRC%/platform/android/*.java %SRC%/game/util/*.java
SET PC_CODE=%SRC%/PCMain.java %SRC%/game/*.java %SRC%/game/board/*.java %SRC%/platform/*.java %SRC%/platform/pc/*.java %SRC%/game/util/*.java %SRC%/game/ui/*.java %SRC%/output/*.java %SRC%/game/save/*.java
SET FILES_CLASSES=%CODE%/*.class %CODE%/game/*.class %CODE%/platform/*.class %CODE%/platform/pc/*.class %CODE%/game/board/*.class %CODE%/game/util/*.class %CODE%/game/ui/*.class %CODE%/output/*.class %CODE%/game/save/*.class

if %BUILD_FOR_ANDROID% == 1 (
   if %DEGUG% == 1 (
      py -3 switch_build.py "Android"

      echo "Generating R.java file"
      %AAPT% package -f -m -0 apk -J "gen" -M AndroidManifest.xml -A "assets" -S "res" -I %PLATFORM_ANDROID% -F "bin\resources.ap_"

      echo "Compiling source code"
      javac -d "obj_android" -classpath "src;gen;obj_android" -sourcepath "gen;src;" -bootclasspath %PLATFORM_ANDROID% -g -source 7 -target   7 -encoding UTF-8 %ANDROID_CODE%
      javac -d "obj_android" -classpath "src;gen;obj_android" -sourcepath "gen;src"  -bootclasspath %PLATFORM_ANDROID% -g -source 7 -target   7 -encoding UTF-8 "gen/com/urquieta/something/R.java"

      echo "Translation bytecode"
      call %DX% --dex --output="bin/classes.dex" "obj_android"

      echo "Making the apk"
      %AAPT% crunch -S "res" -C "res"
      %AAPT% pacakge --no-crunch -f -0 apk -m -F "bin\%APP_NAME%.unaligned.apk" -M AndroidManifest.xml -S "res" -A "assets" -I %PLATFORM_ANDROID%
      mv bin\classes.dex classes.dex
      %AAPT% add -v "bin\%APP_NAME%.unaligned.apk" "classes.dex"
      rm classes.dex

      echo "Align apk"
      %ZIPALIGN% -f 4 "bin\%APP_NAME%.unaligned.apk" "bin\%APP_NAME%.unsigned.apk"

      echo "sign app"
      call %APKSIGNER% sign --ks debug.keystore --key-pass pass:android --ks-pass pass:android --out "bin\%APP_NAME%.apk"  "bin\%APP_NAME%.unsigned.apk"
      py -3 switch_build.py "PC" ))

if %BUILD_FOR_PC% == 1 (
   if %DEGUG% == 1 (
     javac -g -Xdiags:verbose -Xlint:all -d bin %PC_CODE%
     pushd bin
     jar -cvmf ..\MANIFEST.MF Something.jar %FILES_CLASSES%
     popd ))
