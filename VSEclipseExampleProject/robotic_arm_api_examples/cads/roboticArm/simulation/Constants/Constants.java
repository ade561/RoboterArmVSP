package cads.roboticArm.simulation.Constants;

public final class Constants {
    public Constants(){}

    //Robot parameters
    public static final int DEFAULT_POS = 50;
    public static final int CHANGEPOS = 2;
    public static final int BUFFER_SIZE = 1024;
    public static final String IP_ADDRESS = "192.168.178.48";
    public static final int PORT = 8080;
    public static final int MAX_POS = 100;
    public static final int MIN_POS = 0;

    //input MSGs
    public static final int MOVE_RIGHT = 1;
    public static final int MOVE_LEFT = 2;
    public static final int MOVE_UP = 3;
    public static final int MOVE_DOWN = 4;
    public static final int MOVE_FORWARD = 5;
    public static final int MOVE_BACKWARDS = 6;
    public static final int OPEN_GRIP = 7;
    public static final int CLOSE_GRIP = 8;
    public static final int HEARTBEAT = 13;
    public static final int ACK = 14;
    public static final int INITIAL_MSG = 15;

    //Timer
    public static final int MAX_WAIT_TIMER = 250;
    public static final int KEEP_ALIVE_TRIES = 3;
}
