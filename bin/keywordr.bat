@echo off

REM Set the classpath
set CLASSPATH=..\lib\*;..\lib\keywordr-1.0-SNAPSHOT.jar

REM Run the JAR file
call java -cp "%CLASSPATH%" com.keywordr.Main

REM Pause to keep the window open after execution
pause