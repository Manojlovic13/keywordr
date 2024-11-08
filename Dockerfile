# Use a base image with Java installed
FROM openjdk:17-jdk-slim
LABEL authors="manojovic13"

# Copy the configuration files from the local machine to the container
COPY ./config/*.csv /opt/keywordr/configuration/
COPY ./config/*.json /opt/keywordr/configuration/

# Cpoy keyword_configuration.ini to the container
COPY ./config/keywordr_configuration.ini /opt/keywordr/configuration/

# Copy the jar file from the local machine to the container
COPY ./target/*.jar /opt/keywordr/lib/

# Copy the dependency jars from the local machine to the container
COPY ./target/dependency-jars/*.jar /opt/keywordr/lib/

# Set the working directory inside the container
WORKDIR /opt/keywordr/lib/

# Command to execute the jar file
CMD ["java", "-cp", "./*:keywordr-1.0-SNAPSHOT.jar", "com.keywordr.Main"]

# To build a new container run:     docker build -t keywordr-app .
# To run the container run:         docker run -d keywordr-app