package cads.roboticArm.simulation;

import java.net.DatagramPacket;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;
import org.cads.vs.roboticArm.hal.real.CaDSRoboticArmReal;
import java.util.concurrent.TimeUnit;

import java.net.DatagramSocket;
import java.net.InetAddress;

import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;
import org.cads.vs.roboticArm.hal.simulation.CaDSRoboticArmSimulation;

import java.util.concurrent.TimeUnit;


import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Server {
    private static final int BUFFER_SIZE = 1024;
    private static final String IP_ADDRESS = "172.16.1.11";
    private static final int PORT = 8080;
    private static ICaDSRoboticArm simulation;
    
    
    public static void main(String[] args) {
        try {
        	//Roboter erstellen
            simulation = new CaDSRoboticArmReal("172.16.1.64", 50055);
            simulation.init();
            simulation.waitUntilInitIsFinished();
            simulation.setBackForthPercentageTo(50);
            simulation.setLeftRightPercentageTo(50);
            simulation.setUpDownPercentageTo(50);
            simulation.setOpenClosePercentageTo(50);
             //Socket erstellen
            DatagramSocket socket = new DatagramSocket(PORT, InetAddress.getByName(IP_ADDRESS));
            ServerStub stub = new ServerStub(IP_ADDRESS, PORT, socket,simulation);

            HeartbeatSender heartbeatSender = new HeartbeatSender(stub,simulation );
            
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
