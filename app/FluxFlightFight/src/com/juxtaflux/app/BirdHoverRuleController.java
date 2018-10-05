package com.juxtaflux.app;

import com.juxtaflux.fluxlib.Actor;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.function.Consumer;

/** A rule-based controller that makes a {@link Bird} hover in place for a period of time */
/*
A hover rule like this will slingshot/oscillate. Oscillation happens because with a simple rule like this
the further away from the goal you are, the longer the time you have to correct. This means that
by the time you hit the goal, your velocity is very high becuase it is possible to accelerate to a
high velocity.  So, to stop the oscillation, cap the velocity to some constant so that it can't grow without
bounds.
*/
public class BirdHoverRuleController implements Actor {
    private Bird bird;
    private int goalHeight;
    private final double MAX_Y_VEL = -50.0;
    private Consumer<Double> state;

    public BirdHoverRuleController(Bird bird, int goalHeight, double hoverDuration, double quietDuration) {
        this.bird = bird;
        this.goalHeight = goalHeight;
        transitionToQuiet();

        // animate transitions
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(quietDuration), (e) -> transitionToHover()),
                new KeyFrame(Duration.seconds(quietDuration + hoverDuration), (e) -> transitionToQuiet())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void transitionToQuiet() {
        state = (delta) -> {};
    }

    private void transitionToHover() {
        state = this::hoverStep;
    }

    @Override
    public void step(double delta) {
        state.accept(delta);
    }

    public void hoverStep(double delta) {
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
