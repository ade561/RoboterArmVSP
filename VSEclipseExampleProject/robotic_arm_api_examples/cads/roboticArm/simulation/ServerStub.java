package cads.roboticArm.simulation;

import cads.roboticArm.simulation.Interfaces.IServerStub;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

import java.awt.Robot;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class ServerStub implements IServerStub {
    private static int position = 50;
    
    private ResponseSender responseSender;
    private ICaDSRoboticArm robot;
    private int srcPort;
    private int dstPort;
    private int seqNumber;
    private String srcIp;
    private String dstIp;
    private DatagramSocket socket;
    
    public ServerStub(String srcIp, int srcPort, DatagramSocket socket,ICaDSRoboticArm robot) {
        this.srcIp = srcIp;
        this.srcPort = srcPort;
        this.socket = socket;
        this.robot = robot;
        this.responseSender = new ResponseSender(this.socket); 
    }
    
    
  
    private static int changePosition(boolean increase) {
    	if(increase == true && (position + 2) > 100) {
    		return position = 100;
    	}else if(increase != true && (position - 2) < 0) {
    		return position = 0;
    	}
    	return position += (increase ? 2 : -2);
    }
    
    //TODO die aufrufe sollten lieber von einer Actuator Klasse aufgerufen werden statt direkt vom Roboter
    public static void move(ICaDSRoboticArm robot, String direction, boolean increase) {
    	if((!direction.equals("openGrip")) || (!direction.equals("closeGrip"))) {
    		position = changePosition(increase);
    	}
        switch (direction.toLowerCase()) {
            case "leftright":
                robot.setLeftRightPercentageTo(position);
                break;
            case "updown":
                robot.setUpDownPercentageTo(position);
                break;
            case "backforth":
                robot.setBackForthPercentageTo(position);
                break;
            case "opengrip":
            	robot.setOpenClosePercentageTo(100);
            	break;
            case "closegrip":
            	robot.setOpenClosePercentageTo(0);
            	break;
            default:
                System.out.printf("[Warnung] Ungültige Richtung: %s\n", direction);
                break;
        }
    }

    //TODO lieber in einer Dispatcher klasse
    private static void dispatchCommand(int functionId, ICaDSRoboticArm robot) {
        switch (functionId) {
            case 1:
                move(robot, "leftright", true);
                break;
            case 2:
                move(robot, "leftright", false);
                break;
            case 3:
                move(robot, "updown", true);
                break;
            case 4:
                move(robot, "updown", false);
                break;
            case 5:
                move(robot, "backforth", true);
                break;
            case 6:
                move(robot, "backforth", false);
                break;
            case 7:
            	move(robot,"openGrip",true);
            	break;
            case 8:
            	move(robot,"closeGrip",false);
            	break;
            default:
                System.out.printf("[Unbekannte Funktion] ID = %d\n", functionId);
                break;
        }
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
        
        // Führe den Befehl aus
        dispatchCommand(functionId, getICaDSRoboticArm());
    }
    
    public void sendHeartbeat() {
    	responseSender.sendResponse(getDstIp(), getDstPort(), getSrcIp(), getSrcPort(), 13, getSeqNumber());
    }


                
        public String getSrcIp() { return srcIp; }
        public String getDstIp() { return dstIp; }
        public int getSrcPort() { return srcPort; }
        public int getDstPort() { return dstPort; }
        public int getSeqNumber() { return seqNumber; }
        public ICaDSRoboticArm getICaDSRoboticArm() {return robot;}

        public void setSrcIp(String srcIp) { this.srcIp = srcIp; }
        public void setDstIp(String dstIp) { this.dstIp = dstIp; }
        public void setSrcPort(int srcPort) { this.srcPort = srcPort; }
        public void setDstPort(int dstPort) { this.dstPort = dstPort; }
        public void setSeqNumber(int seqNumber) { this.seqNumber = seqNumber; }

}
