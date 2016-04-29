rem Customize this sample file to your system before using it 
rem remark out echo and pause commands when no longer needed 
echo off


rem Section 1. Setting environment variables
rem ----------------------------------------
echo * 
echo Trying to set environment variables ...
pause

rem setting path variable 
rem The setting may be different on your computer 
set PATH=C:\Program Files\Java\jdk1.7.0_75\bin; 

rem setting classpath variable, including current directory (.): 
rem The setting may be different on your computer 
set CLASSPATH=C:\Program Files\Java\jdk1.7.0_75\lib;


rem Section 2. Compiling a demo java program
rem ----------------------------------------
echo * 
echo Trying to compile the Pre Processor java program ...
pause 
javac -d ../../../ -cp ../../../; *.java

Pause 
