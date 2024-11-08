# Use a base image with Java installed
FROM openjdk:17-jdk-slim
LABEL authors="manojovic13"

# Install required tools in container
RUN apt-get update && apt-get install -y unzip

# Copy keywordr delivery from target/
COPY target/keywordr-1.0-SNAPSHOT-dist.zip /opt/

# Unzip the delivery archive
RUN unzip /opt/keywordr-1.0-SNAPSHOT-dist.zip -d /opt/

# Set the working directory inside the container
WORKDIR /opt/keywordr-1.0-SNAPSHOT/bin

# Command to execute the jar file
CMD ["sh", "keywordr.sh"]

# To build a new container run:     docker build -t keywordr-app .
# To run the container run:         docker run -d keywordr-app