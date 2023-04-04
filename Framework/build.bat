@echo off

set classesDir=./target/classes
set classes=./etu2079
set jarDestination=../../framework.jar

cd %classesDir%
jar -cf %jarDestination% %classes%