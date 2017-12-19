package com.juxtaflux.app;

import com.juxtaflux.fluxlib.Actor;

/** A rule-based controller that makes a bird hover in place */
/*
A simple hover rule like this will slingshot/oscillate. Oscillation happens because with a simple rule like this
the further away from the goal you are, the longer the time you have to correct. This means that
by the time you hit the goal, your velocity is very high becuase it is possible to accelerate to a
high velocity.  So, to stop the oscillation, cap the velocity to some constant so that it can't grow without
bounds.
*/
public class BirdHoverRuleController implements Actor {
    private Bird bird;
    private int goalHeight;
    private final double MAX_Y_VEL = -50.0;

    public BirdHoverRuleController(Bird bird, int goalHeight) {
        this.bird = bird;
        this.goalHeight = goalHeight;
    }

    @Override
    public void step(double delta) {
        if (bird.getY() > goalHeight) {
            if (bird.getVel().getY() < MAX_Y_VEL) {
                return;
            }
            bird.handlePressFlap();
            bird.handleReleaseFlap();
        }
    }

    @Override
    public void reapImpl() {
    }

    @Override
    public boolean canReap() {
        return false;
    }
}
