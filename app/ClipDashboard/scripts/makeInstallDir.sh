#!/bin/bash
set -e  # fail entire script on any error
set -u  # treat unset variables as error

OUT_DIR=../out/ClipDashboard

rm -rf $OUT_DIR
mkdir -p $OUT_DIR
cp ../out/artifacts/ClipDashboard/ClipDashboard.jar $OUT_DIR
cp ../src/ClipDashboardIcon.ico $OUT_DIR
cp generateShortcut*.vbs $OUT_DIR
cp launchApp.bat $OUT_DIR
cp launchApp.vbs $OUT_DIR
echo Complete
echo Now, copy the $OUT_DIR folder to the target hardware and then run a generateShortcut script to create a shortcut for launching the app.
