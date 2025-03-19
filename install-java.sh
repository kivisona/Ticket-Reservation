#!/bin/bash
echo "Installing Java 21 (LTS)..."

# Set installation directory
INSTALL_DIR="$HOME/java-21"

# Create directory
mkdir -p $INSTALL_DIR

# Download Java 21 from Adoptium API
wget https://api.adoptium.net/v3/binary/latest/21/ga/linux/x64/jdk/hotspot/normal/eclipse -O java21.tar.gz

# Extract Java
tar -xzf java21.tar.gz -C $INSTALL_DIR --strip-components=1

# Set JAVA_HOME
export JAVA_HOME=$INSTALL_DIR
export PATH=$JAVA_HOME/bin:$PATH

# Verify installation
java -version

echo "Java 21 installed successfully!"
