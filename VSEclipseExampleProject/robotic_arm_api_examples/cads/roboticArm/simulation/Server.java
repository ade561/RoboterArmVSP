package cads.roboticArm.simulation;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;
import org.cads.vs.roboticArm.hal.simulation.CaDSRoboticArmSimulation;

import java.util.concurrent.TimeUnit;


import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Server {
    private static final int BUFFER_SIZE = 1024;
    private static final String IP_ADDRESS = "192.168.178.48";
    private static final int PORT = 8080;
    private static ICaDSRoboticArm simulation;

    public static String readLine() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    public static void main(String[] args) {
        try {
        	//Roboter erstellen
            simulation = new CaDSRoboticArmSimulation();

            // Socket erstellen
            DatagramSocket socket = new DatagramSocket(PORT, InetAddress.getByName(IP_ADDRESS));
            System.out.printf("UDP Server running on Port %d and address %s...\n", PORT, IP_ADDRESS);

            // Nachrichtenverarbeitung
            while (true) {
                byte[] buffer = new byte[BUFFER_SIZE];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                
                // Empfangen der Nachricht
                socket.receive(packet);
                
                // Nachricht verarbeiten
                ServerStub.unmarshallingMessage(buffer, packet.getLength(),simulation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
