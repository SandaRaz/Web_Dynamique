@echo on

set classesDir=./target/classes
set classes=./etu2079
set jarDestination=../../framework.jar
set libDir=..\testFramework\src\main\webapp\WEB-INF\lib

cd %classesDir%
jar -cf %jarDestination% %classes%
cd ../..
xcopy framework.jar %libDir%\framework.jar /Y