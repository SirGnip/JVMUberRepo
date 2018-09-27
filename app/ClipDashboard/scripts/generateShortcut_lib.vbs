' Generic library/tool to create Windows shortcuts for a Java application
'
' Reference: https://superuser.com/a/392082
' Reference: http://ss64.com/nt/shortcut.html
' Reference: https://social.msdn.microsoft.com/Forums/vstudio/en-US/7ad0b629-c723-47e0-b65d-3b1c66f0f010/vbscript-shortcut-targetpath-has-if-string-with-whitespaces-is-used?forum=vbgeneral

If (WScript.Arguments.Count <> 4) Then
    Err.Raise vbObjectError, "", "Required 4 arguments but received " & WScript.Arguments.Count
    'Err.Raise vbObjectError, WScript.ScriptName, "Required 4 command line arguments but received " & WScript.Arguments.Count
End If

shortcutName = WScript.Arguments.Item(0)
targetPath = WScript.Arguments.Item(1)
arguments = WScript.Arguments.Item(2)
iconFile = WScript.Arguments.Item(3)

shortcutName = shortcutName & ".LNK"
set fileSystem = CreateObject("Scripting.FileSystemObject")
curDir = fileSystem.GetAbsolutePathName(".")
WScript.Echo "Generating: " & shortcutName & vbCrLf & _
    "Working Directory: " & curDir & vbCrLf & _
    "Target Path: " & targetPath & vbCrLf & _
    "Arguments: " & arguments & vbCrLf & _
    "Icon File: " & iconFile & vbCrLf & _
    vbCrLf & _
    "This script is meant to be run after the app's folder is in place on the target machine.  It will generate the app's shortcut, specific to its final location."
Set oWS = WScript.CreateObject("WScript.Shell")
Set oLink = oWS.CreateShortcut(shortcutName)
oLink.WorkingDirectory = curDir
oLink.TargetPath = targetPath
If Len(Trim(arguments)) Then
    oLink.Arguments = arguments
End If
If Len(Trim(iconFile)) Then
    oLink.IconLocation = curDir & "\" & iconFile
End If
oLink.Save
