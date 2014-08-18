#!/bin/bash
set -e
adb devices
android update project -p .
cd android-test
android update test-project -m ../ -p .
ant debug install

ant test | {
  while IFS= read -r line
  do
    echo $line
    if [[ $line == *FAIL* ]]
    then
      echo "failure found stop here"
      exit 1
    fi
  done
}
rm build.xml
rm ant.properties
cd ..
rm build.xml

