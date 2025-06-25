package cads.roboticArm.simulation.Interfaces;

public interface IServerStub {

    void unmarshallingMessage(byte[] raw, int len);

    // statische Methoden sind erlaubt, aber nicht private in Java 8
    static int calculateChecksum(byte[] raw, int len) {
        int checksum = 0;
        for (int i = 0; i < len; i++) {
            checksum += (raw[i] & 0xFF);
        }
        return checksum;
    }

    void sendHeartbeat();
}
