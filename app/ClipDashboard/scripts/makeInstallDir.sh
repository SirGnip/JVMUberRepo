#!/bin/bash
OUT_DIR=../out/ClipDashboard
SHORTCUT_SCRIPT=RUN_ONCE_GenerateShortcut.vbs

rm -rf $OUT_DIR
mkdir -p $OUT_DIR
cp ../out/artifacts/ClipDashboard/Clipdashboard.jar $OUT_DIR
cp ../src/ClipDashboardIcon.ico $OUT_DIR
cp $SHORTCUT_SCRIPT $OUT_DIR
echo Complete.
echo Now, copy the $OUT_DIR folder to the target hardware and then run $SHORTCUT_SCRIPT to create shortcut for running.
