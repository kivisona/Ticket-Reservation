#!/bin/bash
echo "Installing Java 23..."

# Set installation directory (must be writable)
INSTALL_DIR="$HOME/java-23"

# Create directory
mkdir -p $INSTALL_DIR

# Download Java 23 from OpenJDK early access
wget https://download.java.net/java/early_access/jdk23/23/GPL/openjdk-23_linux-x64_bin.tar.gz -O java23.tar.gz

# Extract Java
tar -xzf java23.tar.gz -C $INSTALL_DIR --strip-components=1

# Set JAVA_HOME
export JAVA_HOME=$INSTALL_DIR
export PATH=$JAVA_HOME/bin:$PATH

# Verify installation
java -version

echo "Java 23 installed successfully!"
