'displays stdout in cmd window, cmd.exe runs while app is open
'CreateObject("Wscript.Shell").Run "cmd.exe /C java -jar Clipdashboard.jar",3,False

'launches app, no cmd window, cmd.exe runs while app is open
'CreateObject("Wscript.Shell").Run "cmd.exe /C java -jar Clipdashboard.jar",0,False

'launches app, no cmd window, cmd.exe runs while app is open
'CreateObject("Wscript.Shell").Run "cmd.exe /C javaw -jar Clipdashboard.jar",0,False

'Launches app, no cmd window, no persistent cmd.exe
CreateObject("Wscript.Shell").Run "cmd.exe /C start /B javaw -jar Clipdashboard.jar",0,False
