REM Direct route - Cmd window stays open and shows stdout. Disappears when app is closed.
REM java -jar Clipdashboard.jar

REM Direct route - Cmd window stays open and NO stdout. Cmd window disappears when app is closed.
REM javaw -jar Clipdashboard.jar

REM works but there is a brief flicker of a window (no cmd.exe running while app is open)
REM start "" javaw -jar Clipdashboard.jar

REM works, but still brief window flicker (no cmd.exe running while app is open)
start /B "" javaw -jar Clipdashboard.jar
