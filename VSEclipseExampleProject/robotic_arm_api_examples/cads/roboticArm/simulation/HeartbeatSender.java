package cads.roboticArm.simulation;

public class HeartbeatSender extends Thread {
    private final ServerStub serverStub;
    private volatile boolean running = true;

    public HeartbeatSender(ServerStub serverStub) {
        this.serverStub = serverStub;
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Warte, bis eine gültige Zieladresse da ist
                if (serverStub.getDstIp() != null && serverStub.getDstPort() != 0) {
                    serverStub.sendHeartbeat();
                    //System.out.println("[Heartbeat] gesendet an " + serverStub.getDstIp() + ":" + serverStub.getDstPort());
                } else {
                    //System.out.println("[Heartbeat] Kein Client verbunden. Warte...");
                }
                
                Thread.sleep(500); // Warte 2 Sekunden
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

