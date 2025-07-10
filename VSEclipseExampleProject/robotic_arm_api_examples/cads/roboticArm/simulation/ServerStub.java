package cads.roboticArm.simulation;

import cads.roboticArm.simulation.Constants.Constants;
import cads.roboticArm.simulation.Interfaces.IServerStub;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

import java.awt.Robot;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Observable;

public class ServerStub extends Observable implements IServerStub {

    private ResponseSender responseSender;
    private RobotArmActuator robotArmActuator;
    private Dispatcher dispatcher;


    private int srcPort;
    private int dstPort;
    private int seqNumber;
    private String srcIp;
    private String dstIp;
    private DatagramSocket socket;
    
    public ServerStub(String srcIp, int srcPort, DatagramSocket socket,RobotArmActuator robotArmActuator, Dispatcher dispatcher) {
        this.srcIp = srcIp;
        this.srcPort = srcPort;
        this.socket = socket;
        this.robotArmActuator = robotArmActuator;
        this.dispatcher = dispatcher;
        this.responseSender = new ResponseSender(this.socket);
    }

    public void unmarshallingMessage(byte[] raw, int len) {
        // Überprüfe, ob die Nachricht die erwartete Länge hat
        if (len != 24) {
            System.out.printf("[Warnung] Ungültige Nachrichtenlänge: %d (erwartet: 24)\n", len);
            return;
        }

        // Extrahiere die Client-IP (Big Endian)
        String srcIP = String.format("%d.%d.%d.%d", 
            raw[0] & 0xFF, raw[1] & 0xFF, raw[2] & 0xFF, raw[3] & 0xFF);
        // Extrahiere die Server-IP (Big Endian)
        String dstIP = String.format("%d.%d.%d.%d", 
            raw[4] & 0xFF, raw[5] & 0xFF, raw[6] & 0xFF, raw[7] & 0xFF);

        // Extrahiere die Ports (Big Endian)
        int srcPort = ((raw[8] & 0xFF) << 8) | (raw[9] & 0xFF);
        int dstPort = ((raw[10] & 0xFF) << 8) | (raw[11] & 0xFF);

        if (dstPort != getSrcPort() && !(dstIP.equals(getDstIp()))) {
            System.out.printf("[Warnung] nicht für mich bestimmt\n");
            return;
        }
        
        
        // Extrahiere die Funktion ID (Big Endian)
        int functionId = ((raw[12] & 0xFF) << 24) |
                         ((raw[13] & 0xFF) << 16) |
                         ((raw[14] & 0xFF) << 8) |
                         (raw[15] & 0xFF);

        // Extrahiere die Sequence Number (Big Endian)
        int seqNumber = ((raw[16] & 0xFF) << 24) |
                        ((raw[17] & 0xFF) << 16) |
                        ((raw[18] & 0xFF) << 8) |
                        (raw[19] & 0xFF);

        // Überprüfe die Checksumme
        int checksumCalculated = IServerStub.calculateChecksum(raw, len - 4); // Die letzten 4 Bytes sind die Checksumme
        int checksumReceived = ((raw[len - 4] & 0xFF) << 24) |
                               ((raw[len - 3] & 0xFF) << 16) |
                               ((raw[len - 2] & 0xFF) << 8) |
                               (raw[len - 1] & 0xFF);
        
        if (checksumCalculated != checksumReceived) {
            System.out.printf("[Warnung] Ungültige Checksumme: Erwartet %d, empfangen %d\n", checksumCalculated, checksumReceived);
            return;
        }

        
        setDstIp(srcIP);
        setDstPort(srcPort);
        setSeqNumber(seqNumber);

        dispatcher.dispatchCommand(functionId, robotArmActuator);
        dispatcher.notifyWithMessage("CHANGE_POS");
    }
    
    public void sendHeartbeat() {
    	responseSender.sendResponse(getDstIp(), getDstPort(), getSrcIp(), getSrcPort(), Constants.HEARTBEAT, getSeqNumber());
    }
                
        public String getSrcIp() { return srcIp; }
        public String getDstIp() { return dstIp; }
        public int getSrcPort() { return srcPort; }
        public int getDstPort() { return dstPort; }
        public int getSeqNumber() { return seqNumber; }

        public void setSrcIp(String srcIp) { this.srcIp = srcIp; }
        public void setDstIp(String dstIp) { this.dstIp = dstIp; }
        public void setSrcPort(int srcPort) { this.srcPort = srcPort; }
        public void setDstPort(int dstPort) { this.dstPort = dstPort; }
        public void setSeqNumber(int seqNumber) { this.seqNumber = seqNumber; }
        public RobotArmActuator getRobotArmActuator() {return robotArmActuator;}
        public Dispatcher getDispatcher() {return dispatcher;}


}
