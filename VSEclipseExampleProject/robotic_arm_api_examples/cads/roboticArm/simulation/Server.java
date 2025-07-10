package cads.roboticArm.simulation;

import java.net.DatagramPacket;

import cads.roboticArm.simulation.Constants.Constants;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

import java.net.DatagramSocket;
import java.net.InetAddress;

import org.cads.vs.roboticArm.hal.real.CaDSRoboticArmReal;
import org.cads.vs.roboticArm.hal.simulation.CaDSRoboticArmSimulation;
import org.cads.vs.roboticArm.logger.CaDSRoboticArmHALLogLevel;
import org.cads.vs.roboticArm.logger.CaDSRoboticArmHALLogger;

public class Server {

    private static ICaDSRoboticArm roboticArm;
    private static RobotArmActuator robotArmActuator;
    private static RobotArmSensor robotArmSensor;
    private static Dispatcher dispatcher;
    private static HeartbeatReceiver heartbeatReceiver;
    private static HeartbeatSender heartbeatSender;
    private static RobotArmObserver robotArmObserver;
    
    
    public static void main(String[] args) {
        try {
            CaDSRoboticArmHALLogger.init(CaDSRoboticArmHALLogLevel.DEBUG, true, "log.txt");

            //Roboter erstellen
            roboticArm = new CaDSRoboticArmSimulation();
            //roboticArm = new CaDSRoboticArmReal("172.16.1.64", 50055);
            robotArmSensor = new RobotArmSensor(roboticArm);
            robotArmActuator = new RobotArmActuator(roboticArm,robotArmSensor);
            dispatcher = new Dispatcher();

            robotArmActuator.initRobotArm(roboticArm);

             //Socket erstellen
            DatagramSocket socket = new DatagramSocket(Constants.PORT, InetAddress.getByName(Constants.IP_ADDRESS));
            ServerStub stub = new ServerStub(Constants.IP_ADDRESS, Constants.PORT, socket, robotArmActuator,dispatcher);

            heartbeatSender = new HeartbeatSender(stub, roboticArm,dispatcher);
            heartbeatReceiver = new HeartbeatReceiver(stub,roboticArm,dispatcher);
            dispatcher.setHeartbeatReceiver(heartbeatReceiver);

            System.out.printf("UDP Server running on Port %d and address %s...\n", Constants.PORT, Constants.IP_ADDRESS);

            robotArmObserver = new RobotArmObserver(stub);

            stub.addObserver(robotArmObserver);
            robotArmActuator.addObserver(robotArmObserver);
            dispatcher.addObserver(robotArmObserver);

            heartbeatSender.start();
            heartbeatReceiver.startChecking();
            // Nachrichtenverarbeitung
            while (true) {
                byte[] buffer = new byte[Constants.BUFFER_SIZE];
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
