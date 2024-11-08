@echo off
rem build maven project
call mvn clean install

rem build and run docker container
docker build -t keywordr-app .
docker run -d --name keywordr-app keywordr-app

rem check logs
docker wait keywordr-app
docker logs keywordr-app

rem remove container and image
docker rm keywordr-app
docker rmi -f keywordr-app