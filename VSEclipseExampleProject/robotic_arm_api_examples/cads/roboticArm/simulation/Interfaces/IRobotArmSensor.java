package cads.roboticArm.simulation.Interfaces;

public interface IRobotArmSensor {

    int getLeftRightPercentage();

    int getUpDownPercentage();

    int getBackForthPercentage();

    int getOpenCloseGripPercentage();
}
