package cads.roboticArm.simulation;

import cads.roboticArm.simulation.Constants.Constants;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

import java.sql.Timestamp;


import cads.roboticArm.simulation.Constants.Constants;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

public class HeartbeatReceiver {

    private final ServerStub serverStub;
    private final ICaDSRoboticArm robot;
    private final Dispatcher dispatcher;
    private final OneShotTimer timer;

    private volatile long lastHeartbeatTime;

    public HeartbeatReceiver(ServerStub serverStub, ICaDSRoboticArm robot, Dispatcher dispatcher) {
        this.serverStub = serverStub;
        this.robot = robot;
        this.dispatcher = dispatcher;
        this.lastHeartbeatTime = System.currentTimeMillis();
        this.timer = new OneShotTimer(Constants.MAX_WAIT_TIMER);
    }

    public void startChecking() {
        timer.start(() -> {
            try {
                if (serverStub.getDstIp() != null && serverStub.getDstPort() != 0 && robot.heartbeat()) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastHeartbeatTime > Constants.MAX_WAIT_TIMER && !dispatcher.getHeartbeatAck()) {
                        System.out.println("[HeartbeatReceiver] Timeout - Keine ACK Nachricht empfangen.");
                        robot.teardown();
                        timer.stop(); // Timer optional stoppen
                    }
                }
            } catch (Exception e) {
                System.err.println("[HeartbeatReceiver Fehler] " + e.getMessage());
            }
        });
    }

    public void updateHeartbeatTimestamp() {
        lastHeartbeatTime = System.currentTimeMillis();
    }
}

