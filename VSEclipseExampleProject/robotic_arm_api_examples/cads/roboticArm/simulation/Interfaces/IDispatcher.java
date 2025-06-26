package cads.roboticArm.simulation.Interfaces;

import cads.roboticArm.simulation.RobotArmActuator;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

@FunctionalInterface
public interface IDispatcher {
    void dispatchCommand(int functionId, RobotArmActuator robotArmActuator);
}

