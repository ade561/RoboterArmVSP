package cads.roboticArm.simulation;

import cads.roboticArm.simulation.Constants.Constants;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

import java.sql.Timestamp;

//TODO Diese Klasse soll auf die Received ACK vom ITS-BRDs reagieren
public class HeartbeatReceiver extends Thread {

    private final Timestamp timestamp;
    private final ServerStub serverStub;
    private final ICaDSRoboticArm robot;
    private final Dispatcher dispatcher;
    private volatile boolean running = true;

    public HeartbeatReceiver(ServerStub serverStub, ICaDSRoboticArm robot, Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.timestamp = new Timestamp(0);
        this.serverStub = serverStub;
        this.robot = robot;
    }

    @Override
    public void run() {
        while (running) {
            try {
                if (serverStub.getDstIp() != null && serverStub.getDstPort() != 0 &&
                        robot.heartbeat() && timestamp.getTime() > Constants.MAX_WAIT_TIMER && !dispatcher.getHeartbeatAck()) {

                    robot.teardown();
                }
            } catch (Exception e) {
                System.err.println("[Heartbeat Receive Fehler] " + e.getMessage());
            }
        }
    }
}
