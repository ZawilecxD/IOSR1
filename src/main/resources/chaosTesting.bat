@echo off
setlocal EnableDelayedExpansion
set list=
set length=0


for /l %%x in (1, 1, %1) do (
   echo Starting client nr%%x 
   set checker=900%%x
   echo !checker!
   start "Client!checker!" cmd /c java -jar Client-1.0-SNAPSHOT.jar -p !checker!
   set list=!list! !checker!
   set /A length = length+1
   timeout /t 1
)
echo ALL CLIENTS INITIALIZED !length!

timeout /t 1000
:loop
	echo lol

	(for %%a in (%list%) do (
	   set /a id=%random% %%%1 +9001
	   echo Killing !id!
	   taskkill /fi "WindowTitle eq Client!id!"
	   timeout /t 10
	   start "Client!id!" cmd /c java -jar Client-1.0-SNAPSHOT.jar -p !id!
	   echo/
	))
goto loop


endlocal
echo Complete
