#!/bin/bash
set -e  # fail entire script on any error
set -u  # treat unset variables as error

OUT_DIR=../out/FluxFlightFight

rm -rf $OUT_DIR
mkdir -p $OUT_DIR
cp ../out/artifacts/FluxFlightFight/FluxFlightFight.jar $OUT_DIR
cp ../../../3p/JInput/Jenkins-2841/dist/*.dll $OUT_DIR
echo Complete
echo Now, manually copy the $OUT_DIR folder to the target hardware
echo
echo To run...
echo java -jar FluxFlightFight.jar
echo java -cp FluxFlightFight.jar com.juxtaflux.experiments.KitchenSinkExample
echo java -cp FluxFlightFight.jar com.juxtaflux.experiments.AllExamplesRunner
