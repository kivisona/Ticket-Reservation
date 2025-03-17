#!/bin/bash
echo "Installing Java 23..."
wget https://download.java.net/java/GA/jdk23/latest/jdk-23_linux-x64_bin.tar.gz -O jdk23.tar.gz
tar -xzf jdk23.tar.gz
mv jdk-23 /opt/java-23
export JAVA_HOME=/opt/java-23
export PATH=$JAVA_HOME/bin:$PATH
echo "Java 23 installed successfully!"
