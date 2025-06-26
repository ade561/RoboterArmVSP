package cads.roboticArm.simulation;

import java.net.DatagramPacket;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

import java.net.DatagramSocket;
import java.net.InetAddress;

import org.cads.vs.roboticArm.hal.simulation.CaDSRoboticArmSimulation;

public class Server {
    private static final int BUFFER_SIZE = 1024;
    private static final String IP_ADDRESS = "141.22.73.253";
    private static final int PORT = 8080;
    private static ICaDSRoboticArm roboticArm;
    
    
    public static void main(String[] args) {
        try {
        	//Roboter erstellen
            roboticArm = new CaDSRoboticArmSimulation();
            //simulation = new CaDSRoboticArmReal("172.16.1.64", 50055);
            roboticArm.init();
            roboticArm.waitUntilInitIsFinished();
            roboticArm.setBackForthPercentageTo(50);
            roboticArm.setLeftRightPercentageTo(50);
            roboticArm.setUpDownPercentageTo(50);
            roboticArm.setOpenClosePercentageTo(50);
             //Socket erstellen
            DatagramSocket socket = new DatagramSocket(PORT, InetAddress.getByName(IP_ADDRESS));
            ServerStub stub = new ServerStub(IP_ADDRESS, PORT, socket, roboticArm);

            HeartbeatSender heartbeatSender = new HeartbeatSender(stub, roboticArm);
            
            System.out.printf("UDP Server running on Port %d and address %s...\n", PORT, IP_ADDRESS);

            heartbeatSender.start();
            // Nachrichtenverarbeitung
            while (true) {
                byte[] buffer = new byte[BUFFER_SIZE];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                
                // Empfangen der Nachricht
                socket.receive(packet);
                
                // Nachricht verarbeiten
                stub.unmarshallingMessage(buffer, packet.getLength());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
