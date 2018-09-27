#!/bin/bash
OUT_DIR=../out/ClipDashboard

rm -rf $OUT_DIR
mkdir -p $OUT_DIR
cp ../out/artifacts/ClipDashboard/Clipdashboard.jar $OUT_DIR
cp ../src/ClipDashboardIcon.ico $OUT_DIR
cp generateShortcut*.vbs $OUT_DIR
cp launchApp.bat $OUT_DIR
cp launchApp.vbs $OUT_DIR
echo Complete
echo Now, copy the $OUT_DIR folder to the target hardware and then run $SHORTCUT_SCRIPT to create shortcut for running.
