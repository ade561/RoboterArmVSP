package cads.roboticArm.simulation;

import cads.roboticArm.simulation.Interfaces.IRobotArmActuator;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

public class RobotArmActuator implements IRobotArmActuator {




    @Override
    public int changePosition(boolean increase) {
        return 0;
    }

    @Override
    public void move(ICaDSRoboticArm robot, String direction, boolean increase) {

    }
}
