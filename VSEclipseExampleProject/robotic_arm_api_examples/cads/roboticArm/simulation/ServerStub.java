package cads.roboticArm.simulation;

import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ServerStub {
    private static int position = 50;

    private static int changePosition(boolean increase) {
        return position += (increase ? 5 : -5);
    }

    public static void move(ICaDSRoboticArm robot, String direction, boolean increase) {
        position = changePosition(increase);
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
            default:
                System.out.printf("[Warnung] Ungültige Richtung: %s\n", direction);
                break;
        }
    }

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
            default:
                System.out.printf("[Unbekannte Funktion] ID = %d\n", functionId);
                break;
        }
    }

    public static void unmarshallingMessage(byte[] raw, int len, ICaDSRoboticArm robot) {
        if (len != 16) {
            System.out.printf("[Warnung] Ungültige Nachrichtenlänge: %d (erwartet: 16)\n", len);
            return;
        }

        String srcIP = String.format("%d.%d.%d.%d", 
            raw[0] & 0xFF, raw[1] & 0xFF, raw[2] & 0xFF, raw[3] & 0xFF);
        String dstIP = String.format("%d.%d.%d.%d", 
            raw[4] & 0xFF, raw[5] & 0xFF, raw[6] & 0xFF, raw[7] & 0xFF);

        int srcPort = ((raw[8] & 0xFF) << 8) | (raw[9] & 0xFF);
        int dstPort = ((raw[10] & 0xFF) << 8) | (raw[11] & 0xFF);

        int functionId = ((raw[12] & 0xFF) << 24) |
                         ((raw[13] & 0xFF) << 16) |
                         ((raw[14] & 0xFF) << 8) |
                         (raw[15] & 0xFF);

        dispatchCommand(functionId, robot);
    }

    public static String readLine() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }
}
