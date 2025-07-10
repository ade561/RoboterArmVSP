package cads.roboticArm.simulation;

import cads.roboticArm.simulation.Constants.Constants;
import cads.roboticArm.simulation.Interfaces.IDispatcher;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

import java.util.Observable;

public class Dispatcher extends Observable implements IDispatcher {
    private boolean heartbeatAck = false;
    private HeartbeatReceiver heartbeatReceiver;




    public Dispatcher(){
    }

    @Override
    public void dispatchCommand(int functionId, RobotArmActuator robotArmActuator) {
        switch (functionId) {
            case Constants.MOVE_RIGHT -> robotArmActuator.move(robotArmActuator.getRoboticArm(), "leftright", true);
            case Constants.MOVE_LEFT -> robotArmActuator.move(robotArmActuator.getRoboticArm(), "leftright", false);
            case Constants.MOVE_UP -> robotArmActuator.move(robotArmActuator.getRoboticArm(), "updown", true);
            case Constants.MOVE_DOWN -> robotArmActuator.move(robotArmActuator.getRoboticArm(), "updown", false);
            case Constants.MOVE_FORWARD -> robotArmActuator.move(robotArmActuator.getRoboticArm(), "backforth", true);
            case Constants.MOVE_BACKWARDS -> robotArmActuator.move(robotArmActuator.getRoboticArm(), "backforth", false);
            case Constants.OPEN_GRIP -> robotArmActuator.move(robotArmActuator.getRoboticArm(), "openclose", true);
            case Constants.CLOSE_GRIP -> robotArmActuator.move(robotArmActuator.getRoboticArm(), "openclose", false);
            case Constants.DISCONNECT -> {
                heartbeatReceiver.getServerStub().setDstPort(0);
                heartbeatReceiver.getServerStub().setSeqNumber(0);
                heartbeatReceiver.getServerStub().setDstIp(null);
            }
            case Constants.ACK -> {
                setHeartbeatAck(true);
                if(heartbeatReceiver != null) {
                    heartbeatReceiver.updateHeartbeatTimestamp();
                }
            }
            default -> notifyWithMessage(Constants.INVALID_MSG);

        }
    }

    public void setHeartbeatAck(boolean v){
        heartbeatAck = v;
    }
    public boolean getHeartbeatAck(){return heartbeatAck; }
    public void setHeartbeatReceiver(HeartbeatReceiver heartbeatReceiver) {
        this.heartbeatReceiver = heartbeatReceiver;
    }

    public HeartbeatReceiver getHeartbeatReceiver() {
        return heartbeatReceiver;
    }

    public void notifyWithMessage(Object message) {
        setChanged(); // darf hier aufgerufen werden, weil Dispatcher Observable erweitert
        notifyObservers(message);
        clearChanged();
    }

}
