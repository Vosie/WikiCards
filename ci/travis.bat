@echo off
adb devices
call android update project -p .
:: We need to call ant clean at the developing directory.
:: But we don't need it in the fresh new cloned directory.
call ant clean
cd android-test
call android update test-project -m ../ -p .
:: Clean it in developing directory
call ant clean

:: enable emma to dump coverage
call ant emma debug install

call ant emma test

:: dump coverage data
cd bin
type coverage.txt

:: clean useless files
cd ../
del build.xml
del ant.properties

cd ../
del  build.xml

:: After finish run, this to files will be changed.
:: It may cause by line ending symbol is not consistent.
:: So we add a workaround here.
git checkout proguard-project.txt
git checkout android-test/proguard-project.txt
