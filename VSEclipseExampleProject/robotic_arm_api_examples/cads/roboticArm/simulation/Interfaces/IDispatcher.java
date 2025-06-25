package cads.roboticArm.simulation.Interfaces;

import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

@FunctionalInterface
public interface IDispatcher {
    void dispatchCommand(int functionId, ICaDSRoboticArm robot);
}

