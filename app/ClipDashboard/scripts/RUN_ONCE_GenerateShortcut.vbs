' Reference: https://superuser.com/a/392082
' Reference: http://ss64.com/nt/shortcut.html
' Reference: https://social.msdn.microsoft.com/Forums/vstudio/en-US/7ad0b629-c723-47e0-b65d-3b1c66f0f010/vbscript-shortcut-targetpath-has-if-string-with-whitespaces-is-used?forum=vbgeneral

' This is a WIP
set fileSystem = CreateObject("Scripting.FileSystemObject")
curDir = fileSystem.GetAbsolutePathName(".")
WScript.Echo "This script is meant to be run after the app's folder is in place on the target machine.  It will generate the app's shortcut, specific to its final location.  The script will then delete itself."
Set oWS = WScript.CreateObject("WScript.Shell")
sLinkFile = "ClipDashboard.LNK"
Set oLink = oWS.CreateShortcut(sLinkFile)
oLink.TargetPath = """C:\Program Files\Java\jdk1.8.0_45\bin\javaw.exe"""
oLink.Arguments = "-jar ClipDashboard.jar"
oLink.WorkingDirectory = curDir
oLink.IconLocation = curDir & "\ClipDashboardIcon.ico"
oLink.Save
intAnswer = Msgbox("Do you want to delete " & WScript.ScriptName & ", as it is mostly a run-once script?", vbYesNo, "Delete Script File")
If intAnswer = vbYes Then
    fileSystem.DeleteFile(WScript.ScriptName)
    WScript.Echo "Deleted " & WScript.ScriptName
End If
