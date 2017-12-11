
echo This makes a directory that you can copoy to any machine and run the app

mkdir ..\out\runnable
copy ..\out\artifacts\FluxFlightFight\*.jar ..\out\runnable\

(
echo echo Running app
echo "c:\Program Files\java\jdk1.8.0_112\bin\java" -cp FluxFlightFight.jar com.juxtaflux.experiments.KitchenSinkExample
echo echo DONE
echo pause
echo echo DONE DONE
) > ..\out\runnable\start.bat

echo "Moving to target location (really only for testing)"
move ..\out\runnable c:\_local\
