#!/bin/bash

# Set the classpath
CLASSPATH="../lib/*:../lib/keywordr-1.0-SNAPSHOT.jar"

# Run the JAR file
java -cp "$CLASSPATH" com.keywordr.Main

# Pause to keep the terminal open after execution
read -p "Press any key to continue..."
