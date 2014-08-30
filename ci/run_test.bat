@echo off

echo wait for emulator
call ci/wait_for_emulator.bat

echo run test
call ci/travis.bat
