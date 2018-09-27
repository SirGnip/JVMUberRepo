' drawback of this approach to creating shortcuts:
' - doesn't work: java displays errors when launching this shortcut because Windows shortcuts cant do relative pathnames
'     - Example: "Error: Registry key 'Software\JavaSoft\Java Runtime Enviornment\CurrentVersion' has value '1.8', but '1.7' is required.

Set objShell = Wscript.CreateObject("WScript.Shell")
objShell.Run "generateShortcut_lib.vbs ClipDashboard javaw ""-jar ClipDashboard.jar"" ""ClipDashboardIcon.ico""", 4, True
