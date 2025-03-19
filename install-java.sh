#!/bin/bash
echo "Installing Java 23..."

# Create installation directory
mkdir -p /opt/java-23

# Download Java 23 from a working source
wget https://download.bell-sw.com/java/23/bellsoft-jdk23-linux-amd64.tar.gz -O java23.tar.gz

# Extract Java
tar -xzf java23.tar.gz -C /opt/java-23 --strip-components=1

# Set JAVA_HOME
export JAVA_HOME=/opt/java-23
export PATH=$JAVA_HOME/bin:$PATH

echo "Java 23 installed successfully!"
java -version
