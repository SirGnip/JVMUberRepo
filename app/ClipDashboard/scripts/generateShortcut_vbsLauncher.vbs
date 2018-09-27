' One drawback of this approach to creating shortcuts:
' - when shortcut is pinned to taskbar and app is launched, the app's icon is the standard java icon and it isn't in the same slot with the taskbar icon.

Set objShell = WScript.CreateObject("WScript.Shell")
objShell.Run "generateShortcut_lib.vbs ClipDashboard %windir%\system32\wscript.exe launchApp.vbs ""ClipDashboardIcon.ico""", 4, True
