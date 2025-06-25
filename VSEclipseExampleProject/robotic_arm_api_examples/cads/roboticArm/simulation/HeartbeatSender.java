package cads.roboticArm.simulation;

import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

public class HeartbeatSender extends Thread {
    private final ServerStub serverStub;
    private final ICaDSRoboticArm robot;
    private volatile boolean running = true;

    public HeartbeatSender(ServerStub serverStub,ICaDSRoboticArm robot) {
        this.serverStub = serverStub;
        this.robot = robot;
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Warte, bis eine gültige Zieladresse da ist
                if (serverStub.getDstIp() != null && serverStub.getDstPort() != 0 && robot.heartbeat() == true) {
                    serverStub.sendHeartbeat();
                } else {
                    System.out.println("[Heartbeat] Kein Client verbunden. Warte...");
                }
                
                Thread.sleep(250); // Warte 2 Sekunden
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("[Heartbeat Fehler] " + e.getMessage());
            }
        }
    }

    public void stopHeartbeat() {
        running = false;
        this.interrupt();
    }
}

