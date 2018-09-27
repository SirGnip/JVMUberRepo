' drawback of this approach to creating shortcuts:
' - can't pin to taskbar (CAN shift+right-click shortcut to pin to Start menu)
' - icon shown on taskbar while running is standard java icon

Set objShell = Wscript.CreateObject("WScript.Shell")
set fileSystem = CreateObject("Scripting.FileSystemObject")
curDir = fileSystem.GetAbsolutePathName(".")
targetPath = curDir & "\Clipdashboard.jar"
objShell.Run "generateShortcut_lib.vbs ClipDashboard " & targetPath & " """" ClipDashboardIcon.ico", 4, True
