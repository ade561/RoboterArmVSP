#!/bin/bash

# Pfad zu JavaFX (bitte Pfad anpassen, falls er bei dir anders ist)
JAVAFX_PATH="D:\javafx-sdk-17.0.15\lib"

java \
  --module-path "$JAVAFX_PATH" \
  --add-modules javafx.controls,javafx.fxml \
  -cp CaDSRMI.jar \
  org.cads.vs.roboticArm.rmi.provider.CaDSRMIProvider \
  -h 127.0.0.1 -S true -p 8888 -ID rmiExampleSimulation
