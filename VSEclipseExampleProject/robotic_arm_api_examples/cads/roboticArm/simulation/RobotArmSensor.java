package cads.roboticArm.simulation;

import cads.roboticArm.simulation.Interfaces.IRobotArmSensor;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

public class RobotArmSensor implements IRobotArmSensor {
    private ICaDSRoboticArm robot;

    public RobotArmSensor(ICaDSRoboticArm robot) {
        this.robot = robot;
    }


    @Override
    public int getLeftRightPercentage() {
        return robot.getLeftRightPercentage();
    }

    @Override
    public int getUpDownPercentage() {
        return robot.getUpDownPercentage();
    }

    @Override
    public int getBackForthPercentage() {
        return robot.getBackForthPercentage();
    }

    @Override
    public int getOpenCloseGripPercentage() {
        return robot.getOpenClosePercentage();
    }
}
