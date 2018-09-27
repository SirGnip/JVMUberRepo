' drawback of this approach to creating shortcuts:
' - need to specify an explicit path to FDK
' Advantage:
' - Can pin to the taskbar and the icon works as expected

Set objShell = Wscript.CreateObject("WScript.Shell")
objShell.Run "generateShortcut_lib.vbs ClipDashboard ""C:\Program Files\Java\jdk1.8.0_181\bin\javaw.exe"" ""-jar ClipDashboard.jar"" ""ClipDashboardIcon.ico""", 4, True
