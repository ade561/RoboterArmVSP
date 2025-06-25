package cads.roboticArm.simulation.Interfaces;

import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

public interface IRobotArmActuator {
    int changePosition(boolean increase);
    void move(ICaDSRoboticArm robot, String direction, boolean increase);
}
