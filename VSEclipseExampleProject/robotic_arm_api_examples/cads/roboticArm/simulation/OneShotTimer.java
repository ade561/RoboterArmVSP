package cads.roboticArm.simulation;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OneShotTimer {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final long intervalMillis;

    public OneShotTimer(long intervalMillis) {
        this.intervalMillis = intervalMillis;
    }

    public void start(Runnable task) {
        scheduler.scheduleAtFixedRate(task, 0, intervalMillis, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        scheduler.shutdownNow();
    }
}