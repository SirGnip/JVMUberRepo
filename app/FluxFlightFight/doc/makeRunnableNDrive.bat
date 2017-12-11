@echo off
echo This makes a directory that you can copoy to any machine and run the app

mkdir ..\out\runnable
copy ..\out\artifacts\FluxFlightFight\*.jar ..\out\runnable\

(
echo echo Running app
echo "c:\Program Files\java\jre1.8.0_152\bin\java" -cp FluxFlightFight.jar com.juxtaflux.experiments.KitchenSinkExample
echo echo DONE
echo pause
echo echo DONE DONE
) > ..\out\runnable\start.bat

echo "You need to manually copy out\runnable to N: now"
pause
xcopy n:\_code\Java\3p\JInput\Jenkins-2841\dist\*.dll n:\_datatemp\runnable