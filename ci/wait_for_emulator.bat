set bootanim=""
set failcounter=0
:loop
	echo bootanim now is %bootanim% and wait for 10 seconds.
	@ping 127.0.0.1 -n 10 -w 1000 > nul

	adb -e shell getprop init.svc.bootanim > temp.txt 2>&1
	set /p bootanim=<temp.txt
	::for /f %%i in ('adb -e shell getprop init.svc.bootanim') do set bootanim=%%i

	if "%bootanim%" == "error: device not found" set /a failcounter+=1
	if %failcounter% GTR 5 (
		 echo "Failed to start emulator"
		 exit 1
	)
if not "%bootanim%" == "stopped" goto loop

:done
echo Done
del temp.txt
