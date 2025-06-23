package cads.roboticArm.simulation;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ResponseSender {

    private DatagramSocket socket;

    // Neuer Konstruktor mit Socket
    public ResponseSender(DatagramSocket socket) {
        this.socket = socket;
    }

    public void sendResponse(String dstIP, int dstPort, String srcIP, int srcPort,
                             int functionId, int seqNumber) {
        try {
            byte[] message = new byte[24];

            // 1. Src-IP
            String[] srcParts = srcIP.split("\\.");
            for (int i = 0; i < 4; i++) {
                message[i] = (byte) Integer.parseInt(srcParts[i]);
            }

            // 2. Dst-IP
            String[] dstParts = dstIP.split("\\.");
            for (int i = 0; i < 4; i++) {
                message[4 + i] = (byte) Integer.parseInt(dstParts[i]);
            }

            // 3. Ports
            message[8] = (byte) (srcPort >> 8);
            message[9] = (byte) (srcPort);
            message[10] = (byte) (dstPort >> 8);
            message[11] = (byte) (dstPort);

            // 4. Function ID
            message[12] = (byte) (functionId >> 24);
            message[13] = (byte) (functionId >> 16);
            message[14] = (byte) (functionId >> 8);
            message[15] = (byte) (functionId);

            // 5. Sequence Number
            message[16] = (byte) (seqNumber >> 24);
            message[17] = (byte) (seqNumber >> 16);
            message[18] = (byte) (seqNumber >> 8);
            message[19] = (byte) (seqNumber);

            // 6. Checksum
            int checksum = 0;
            for (int i = 0; i < 20; i++) {
                checksum += message[i] & 0xFF;
            }
            message[20] = (byte) (checksum >> 24);
            message[21] = (byte) (checksum >> 16);
            message[22] = (byte) (checksum >> 8);
            message[23] = (byte) (checksum);

            // 7. UDP senden über den übergebenen Socket
            InetAddress address = InetAddress.getByName(dstIP);
            DatagramPacket packet = new DatagramPacket(message, message.length, address, dstPort);

            socket.send(packet);

            System.out.printf("[info] Antwort gesendet an %s:%d (Funktion: %d, Seq: %d)\n", dstIP, dstPort, functionId, seqNumber);

        } catch (Exception e) {
            System.err.println("[Fehler beim Senden der Antwort]");
            e.printStackTrace();
        }
    }
}
