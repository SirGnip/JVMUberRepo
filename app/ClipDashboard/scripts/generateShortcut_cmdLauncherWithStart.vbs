' drawback of this approach to creating shortcuts:
' - standard java app icon and ClipDashboard icon both appear

Set objShell = Wscript.CreateObject("WScript.Shell")
objShell.Run "generateShortcut_lib.vbs ClipDashboard ""%windir%\system32\cmd.exe"" ""/c start javaw -jar Clipdashboard.jar"" ""ClipDashboardIcon.ico""", 4, True
