#!/bin/bash

PROJECTNAME=HelloWorld
CLASSPATH=../lib/Kindlet-2.1.jar
export CLASSPATH

echo "Making $PROJECTNAME..."

if [ -f developer.keystore ]; then
  echo "developer.keystore exists!"
else
  echo "Generating developer.keystore..."
  keytool -genkeypair -keystore developer.keystore -storepass password -keypass password -alias dk$PROJECTNAME -dname "CN=Unknown, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown" -validity 5300
  keytool -genkeypair -keystore developer.keystore -storepass password -keypass password -alias di$PROJECTNAME -dname "CN=Unknown, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown" -validity 5300
  keytool -genkeypair -keystore developer.keystore -storepass password -keypass password -alias dn$PROJECTNAME -dname "CN=Unknown, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown" -validity 5300
fi

echo "Compiling..."
javac *.java

echo "Creating jar file..."
/bin/rm $PROJECTNAME.jar >&/dev/null
/bin/rm $PROJECTNAME.azw2 >&/dev/null
jar cvmf $PROJECTNAME.manifest $PROJECTNAME.jar *.class

echo "Creating signed azw2 file..."

jarsigner -keystore developer.keystore -storepass password $PROJECTNAME.jar dk$PROJECTNAME
jarsigner -keystore developer.keystore -storepass password $PROJECTNAME.jar di$PROJECTNAME
jarsigner -keystore developer.keystore -storepass password $PROJECTNAME.jar dn$PROJECTNAME

mv $PROJECTNAME.jar $PROJECTNAME.azw2

echo "Done."