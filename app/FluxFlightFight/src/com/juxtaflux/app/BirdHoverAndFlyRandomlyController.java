package com.juxtaflux.app;

import java.util.Random;

/** A rule-based controller that makes a {@link Bird} hover at a random level, fly to the side, slow down, drop */
public class BirdHoverAndFlyRandomlyController extends BirdHoverAndFlyController {
    private int goalHeightMin;
    private int goalHeightMax;

    public BirdHoverAndFlyRandomlyController(Bird bird, int goalHeightMin, int goalHeightMax, double hoverDur, double quietDur, double horizAccelDur) {
        super(bird, 0, hoverDur, quietDur, horizAccelDur);
        this.goalHeightMin = goalHeightMin;
        this.goalHeightMax = goalHeightMax;
    }

    @Override
    protected void transitionToHover() {
        goalHeight = new Random().nextInt(goalHeightMax - goalHeightMin) + goalHeightMin;
        state = this::hoverStep;
    }
}
