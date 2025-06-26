package cads.roboticArm.simulation;

import cads.roboticArm.simulation.Constants.Constants;
import cads.roboticArm.simulation.Interfaces.IRobotArmActuator;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

import java.util.Locale;

public class RobotArmActuator implements IRobotArmActuator {
    private ICaDSRoboticArm roboticArm;
    private RobotArmSensor robotArmSensor;

    public RobotArmActuator(ICaDSRoboticArm roboticArm, RobotArmSensor robotArmSensor){
        this.roboticArm = roboticArm;
        this.robotArmSensor = robotArmSensor;
    }


    @Override
    public int checkValidPos(boolean increase, int pos) {
        if(increase && pos + Constants.CHANGEPOS > Constants.MAX_POS){
            return Constants.MAX_POS;
        }else if(!increase && pos - Constants.CHANGEPOS < Constants.MIN_POS){
            return Constants.MIN_POS;
        }
        return (increase ? (pos + Constants.CHANGEPOS) : (pos - Constants.CHANGEPOS));
    }

    @Override
    public void move(ICaDSRoboticArm robot, String direction, boolean increase) {
        int pos;
        switch (direction.toLowerCase()){
            case "leftright":
                pos = checkValidPos(increase,robotArmSensor.getLeftRightPercentage());
                roboticArm.setLeftRightPercentageTo(pos);
                break;
            case "updown":
                pos = checkValidPos(increase,robotArmSensor.getUpDownPercentage());
                roboticArm.setUpDownPercentageTo(pos);
                break;
            case "backforth":
                pos = checkValidPos(increase,robotArmSensor.getBackForthPercentage());
                roboticArm.setBackForthPercentageTo(pos);
                break;
            case "openclose":
                pos = checkValidPos(increase,robotArmSensor.getOpenCloseGripPercentage());
                roboticArm.setOpenClosePercentageTo(pos);
                break;
            default:
                System.out.printf("[WARNING]: unbekannter Befehl");
                break;
        }
    }

    @Override
    public void initRobotArm(ICaDSRoboticArm robot) {
        robot.init();
        robot.waitUntilInitIsFinished();
        robot.setBackForthPercentageTo(Constants.DEFAULT_POS);
        robot.setLeftRightPercentageTo(Constants.DEFAULT_POS);
        robot.setOpenClosePercentageTo(Constants.DEFAULT_POS);
        robot.setUpDownPercentageTo(Constants.DEFAULT_POS);
    }


    public ICaDSRoboticArm getRoboticArm() {
        return roboticArm;
    }

    public RobotArmSensor getRobotArmSensor() {
        return robotArmSensor;
    }

}
