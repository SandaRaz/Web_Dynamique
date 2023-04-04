@echo off

set projectDir=./target/test-framework-1.0-SNAPSHOT
set destiDir=../..
set tomcatDir=E:\Sanda\ITU\Prog-Reseaux\apache-tomcat-10.1.2
set warFile=aaa.war

cd %projectDir%
jar -cvf %destiDir%/%warFile% *
cd %destiDir%

xcopy %warFile% %tomcatDir%\webapps /Y
del %warFile%

cd %tomcatDir%\bin
startup.bat 
