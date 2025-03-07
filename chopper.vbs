' Chopper v1

Set fs = CreateObject("Scripting.FileSystemObject")
Set shell = CreateObject("WScript.Shell")

Dim folder
folder = "C:\Windows\Temp\chopper"
Dim shortcut
shortcut = Shell.SpecialFolders("Desktop") & "\chopper.lnk"
Dim success
success = 0

If Not fs.FolderExists(folder) Then
	fs.CreateFolder(folder)
	success = 1
End If

If Not fs.FileExists(shortcut) Then
	Set link = Shell.CreateShortcut(shortcut)
	link.TargetPath = folder
	link.Save()
	success = 1
End If

If success Then
	MsgBox "Successfully installed!", 64, "Chopper"
End If
