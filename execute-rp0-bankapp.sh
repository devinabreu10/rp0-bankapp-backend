#!/bin/bash

# Set environment variables
ENV_FILE="./.env/local.env"

# Check if the .env file exists
if [ -f "$ENV_FILE" ]; then
    # Export environment variables from the .env file
    export $(grep -v '^#' "$ENV_FILE" | xargs)
else
    echo "Error: .env file not found at $ENV_FILE"
    exit 1
fi

# Path to your JAR file
JAR_PATH="./target/rp0-bankapp-backend-1.0.0.jar"

# Check if the JAR file exists
if [ ! -f "$JAR_PATH" ]; then
    echo "Error: JAR file not found at $JAR_PATH"
    exit 1
fi

echo "Executing rp0-bankapp..."

# Run the Java application
java -jar "$JAR_PATH"
