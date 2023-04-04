@echo off

set classesDir=./target/Framework-1.0-SNAPSHOT/WEB-INF/classes
set classes=./etu2079
set jarDestination=../../../../framework.jar

cd %classesDir%
jar -cf %jarDestination% %classes%