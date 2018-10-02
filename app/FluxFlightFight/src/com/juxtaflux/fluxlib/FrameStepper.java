package com.juxtaflux.fluxlib;

import javafx.animation.AnimationTimer;
import javafx.util.Duration;

import static com.google.common.base.Preconditions.checkArgument;

/** Steps a Stepable to drive a simulation */
/* Ideas: have a multi-window rolling timer for fps calculation?
    - time based? like 1s/5s/10s/all?
    - ticked based? like 60 frames/500 frames/100000 frames/all
 */
public class FrameStepper {
    private static final int NANOS_PER_SEC = 1000000000;
    private int totalFrameCount = 0;
    private Duration startTime = Duration.UNKNOWN;
    private Duration lastTime = Duration.UNKNOWN;
    private Duration intervalTime = Duration.UNKNOWN;
    private int intervalFrameCount = 0;
    Stepable stepable;

    public FrameStepper(Stepable stepable) {
        this.stepable = stepable;
        startTime = Duration.seconds((double) System.nanoTime() / NANOS_PER_SEC);
        lastTime = startTime;
        restartInterval();
    }

    public FrameStepper register() {
        new AnimationTimer() {
            @Override
            public void handle(long nanoTimestamp) {
                ++totalFrameCount;
                ++intervalFrameCount;
                Duration curTime = Duration.seconds((double) nanoTimestamp / NANOS_PER_SEC);
                stepable.step(curTime.subtract(lastTime).toSeconds());
                lastTime = curTime;
            }
        }.start();
        return this;
    }

    public int getTotalFrameCount() {
        return totalFrameCount;
    }

    public Duration getTotalDuration() {
        return lastTime.subtract(startTime);
    }

    public double getTotalFps() {
        checkArgument(!lastTime.equals(startTime), "lastTime and startTime can not be equal");
        return totalFrameCount / getTotalDuration().toSeconds();
    }

    private void restartInterval() {
        intervalFrameCount = 0;
        intervalTime = lastTime;
    }

    public int getIntervalFrameCount() {
        return intervalFrameCount;
    }

    public Duration getIntervalDuration() {
        return lastTime.subtract(intervalTime);
    }

    /** Get fps for interval. Querying for this FPS triggers a new interval. */
    public double getIntervalFps() {
        checkArgument(!lastTime.equals(intervalTime), "lastTime and intervalTime can not be equal");
        double fps = intervalFrameCount / getIntervalDuration().toSeconds();
        return fps;
    }

    public String getFpsSummary() {
        String msg = String.format("last (%d frames):%.2f  all (%d frames):%.2f",
                getIntervalFrameCount(),
                getIntervalFps(),
                getTotalFrameCount(),
                getTotalFps());
        restartInterval();
        return msg;
    }
}
