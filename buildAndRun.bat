@echo off
rem build maven project
call mvn clean install

rem build and run docker container with attached volume for result file
docker build -t keywordr-app .
docker run -d -v C:\keywordr:/opt/keywordr-1.0-SNAPSHOT/result --name keywordr-app keywordr-app

rem check logs
docker wait keywordr-app
docker logs keywordr-app

rem remove container and image
docker rm keywordr-app
docker rmi -f keywordr-app