package cads.roboticArm.simulation;

import cads.roboticArm.simulation.Constants.Constants;
import org.cads.vs.roboticArm.logger.CaDSRoboticArmHALLogLevel;
import org.cads.vs.roboticArm.logger.CaDSRoboticArmHALLogger;

import java.util.Observable;
import java.util.Observer;

public class RobotArmObserver implements Observer{
    private final ServerStub stub;

    public RobotArmObserver(ServerStub stub) {
        this.stub = stub;
    }

    @Override
    public void update(Observable o, Object arg) {
        logCurrentStatus();

    }

    private void logCurrentStatus() {
        // Holen der aktuellen Positionen des Roboterarms
        int leftRight = stub.getRobotArmActuator().getRoboticArm().getLeftRightPercentage();
        int upDown = stub.getRobotArmActuator().getRoboticArm().getUpDownPercentage();
        int backForth = stub.getRobotArmActuator().getRoboticArm().getBackForthPercentage();
        int grip = stub.getRobotArmActuator().getRoboticArm().getOpenClosePercentage();

        if (leftRight == Constants.MAX_POS || leftRight == Constants.MIN_POS) {
            CaDSRoboticArmHALLogger.log(CaDSRoboticArmHALLogLevel.WARNING, "RoboterArmPositionObserver",
                    "Warnung: Left/Right Position des Roboterarms hat den kritischen Wert erreicht: " + leftRight + "%");
        }

        if (upDown == Constants.MAX_POS || upDown == Constants.MIN_POS) {
            CaDSRoboticArmHALLogger.log(CaDSRoboticArmHALLogLevel.WARNING, "RoboterArmPositionObserver",
                    "Warnung: Up/Down Position des Roboterarms hat den kritischen Wert erreicht: " + upDown + "%");
        }

        if (backForth == Constants.MAX_POS || backForth == Constants.MIN_POS) {
            CaDSRoboticArmHALLogger.log(CaDSRoboticArmHALLogLevel.WARNING, "RoboterArmPositionObserver",
                    "Warnung: Back/Forth Position des Roboterarms hat den kritischen Wert erreicht: " + backForth + "%");
        }

        if (grip == Constants.MAX_POS || grip == Constants.MIN_POS) {
            CaDSRoboticArmHALLogger.log(CaDSRoboticArmHALLogLevel.WARNING, "RoboterArmPositionObserver",
                    "Warnung: Grip Position des Roboterarms hat den kritischen Wert erreicht: " + grip + "%");
        }

        if(!stub.getDispatcher().getHeartbeatAck()){
            CaDSRoboticArmHALLogger.log(CaDSRoboticArmHALLogLevel.CRITICAL, "RoboterArmPositionObserver",
                    "Kritisch: Heartbeat ACK wurde nicht empfangen!");
        }

        if(!stub.getRobotArmActuator().getRoboticArm().heartbeat()){
            CaDSRoboticArmHALLogger.log(CaDSRoboticArmHALLogLevel.CRITICAL, "RoboterArmPositionObserver",
                    "Kritisch: Roboter hat kein Heartbeat!");
        }

        if(stub.getDispatcher().getHeartbeatReceiver().getAckCounter() > Constants.KEEP_ALIVE_TRIES){
            CaDSRoboticArmHALLogger.log(CaDSRoboticArmHALLogLevel.CRITICAL, "RoboterArmPositionObserver",
                    "Kritisch: Roboter ist nicht verbunden!");
        }

        // Protokolliere die Position des Roboterarms mit dem CaDS-Logger
        CaDSRoboticArmHALLogger.log(CaDSRoboticArmHALLogLevel.DEBUG, "RoboterArmPositionObserver",
                "Aktuelle Position des Roboterarms: " +
                        "Left/Right: " + leftRight + "%, " +
                        "Up/Down: " + upDown + "%, " +
                        "Back/Forth: " + backForth + "%, " +
                        "Grip: " + grip + "%");
    }
}
