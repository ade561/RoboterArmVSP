package cads.roboticArm.simulation;

import cads.roboticArm.simulation.Constants.Constants;
import cads.roboticArm.simulation.Interfaces.IDispatcher;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

public class Dispatcher implements IDispatcher {
    private RobotArmActuator robotArmActuator;

    public Dispatcher(RobotArmActuator robotArmActuator){
        this.robotArmActuator = robotArmActuator;
    }

    @Override
    public void dispatchCommand(int functionId, RobotArmActuator robotArmActuator) {
        switch (functionId) {
            case Constants.MOVE_RIGHT:
                robotArmActuator.move(robotArmActuator.getRoboticArm(), "leftright", true);
                break;
            case Constants.MOVE_LEFT:
                robotArmActuator.move(robotArmActuator.getRoboticArm(), "leftright", false);
                break;
            case Constants.MOVE_UP:
                robotArmActuator.move(robotArmActuator.getRoboticArm(), "updown", true);
                break;
            case Constants.MOVE_DOWN:
                robotArmActuator.move(robotArmActuator.getRoboticArm(), "updown", false);
                break;
            case Constants.MOVE_FORWARD:
                robotArmActuator.move(robotArmActuator.getRoboticArm(), "backforth", true);
                break;
            case Constants.MOVE_BACKWARDS:
                robotArmActuator.move(robotArmActuator.getRoboticArm(), "backforth", false);
                break;
            case Constants.OPEN_GRIP:
                robotArmActuator.move(robotArmActuator.getRoboticArm(), "openGrip", true);
                break;
            case Constants.CLOSE_GRIP:
                robotArmActuator.move(robotArmActuator.getRoboticArm(), "closeGrip", false);
                break;
            default:
                System.out.printf("[Unbekannte Funktion] ID = %d\n", functionId);
                break;
        }
    }
}
