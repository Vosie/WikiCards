#!/bin/bash
set -e
adb devices
android update project -p .
# We need to call ant clean at the developing directory.
# But we don't need it in the fresh new cloned directory.
ant clean
cd android-test
android update test-project -m ../ -p .
# Clean it in developing directory
ant clean
# enable emma to dump coverage
ant emma debug install

ant emma test | {
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

# dump coverage data
coverageFile="bin/coverage.txt"
if [ -f "$coverageFile" ]
then
  cat $coverageFile
else
  echo "no coverage file found"
fi
# clean useless files
rm build.xml
rm ant.properties
cd ..
rm build.xml

