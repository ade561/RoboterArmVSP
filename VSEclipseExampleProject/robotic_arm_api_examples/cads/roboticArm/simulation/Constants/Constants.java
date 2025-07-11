package cads.roboticArm.simulation.Constants;

public final class Constants {
    public Constants(){}

    //Robot parameters
    public static final int DEFAULT_POS = 50;
    public static final int DEFAULT_GRIP = 100;

    public static final int CHANGEPOS = 2;
    public static final int BUFFER_SIZE = 1024;
    public static final String IP_ADDRESS = "172.16.1.8";
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
    public static final int DISCONNECT = 15;

    //Observer MSG
    public static final String DISCONNECT_STRING = "Disconnect";
    public static final String HEARTBEAT_ERROR = "HeartbeatError";
    public static final String LOST_ACK =  "LostAck";
    public static final String CHANGE_POS = "ChangePos";
    public static final String INVALID_MSG = "invalidMsg";
    public static final String TIMECHECK =  "TimeCheck";
    public static final String CRITICAL_TIMECHECK =  "CriticalTimeCheck";


    //Timer
    public static final int MAX_WAIT_TIMER = 250;
    public static final int KEEP_ALIVE_TRIES = 3;
}
