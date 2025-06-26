package cads.roboticArm.simulation.Interfaces;

import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

public interface IRobotArmActuator {
    int checkValidPos(boolean increase, int pos);
    void move(ICaDSRoboticArm robot, String direction, boolean increase);
    void initRobotArm(ICaDSRoboticArm robtot);
}
