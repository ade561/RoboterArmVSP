package cads.roboticArm.simulation;

import cads.roboticArm.simulation.Interfaces.IDispatcher;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

public class Dispatcher implements IDispatcher {

    @Override
    public void dispatchCommand(int functionId, ICaDSRoboticArm robot) {
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
                move(robot, "openGrip", true);
                break;
            case 8:
                move(robot, "closeGrip", false);
                break;
            default:
                System.out.printf("[Unbekannte Funktion] ID = %d\n", functionId);
                break;
        }
    }

    // IST NUR EIN TEMPORÄR DIE RICHTIGE MOVE IST DANN IN ACTUATOR
    private void move(ICaDSRoboticArm robot, String direction, boolean positive) {
        // Hier kommt deine Steuerlogik rein
        System.out.printf("Bewege %s in Richtung %s\n", direction, positive ? "positiv" : "negativ");
    }
}
