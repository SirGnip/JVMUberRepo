package com.juxtaflux.app;

import com.juxtaflux.fluxlib.Actor;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.function.Consumer;

/** A rule-based controller that makes a {@link Bird} hover, fly to the side, slow down, drop */
public class BirdHoverAndFlyController implements Actor {
    private Bird bird;
    private int goalHeight;
    private final double MAX_Y_VEL = -50.0;
    private Consumer<Double> state;

    public BirdHoverAndFlyController(Bird bird, int goalHeight, double hoverDur, double quietDur, double horizAccelDur) {
        this.bird = bird;
        this.goalHeight = goalHeight;
        transitionToQuiet();

        // animate transitions
        Timeline timeline = new Timeline();
        double dur = 0.0;
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(dur += quietDur), (e) -> transitionToHover()),
                new KeyFrame(Duration.seconds(dur += hoverDur), (e) -> startPressRight()),
                new KeyFrame(Duration.seconds(dur += horizAccelDur), (e) -> stopPressRight()),
                new KeyFrame(Duration.seconds(dur += hoverDur), (e) -> startPressLeft()), // this is an unreliable way to stop horizontal acceleration. But, it is simple.
                new KeyFrame(Duration.seconds(dur += horizAccelDur), (e) -> stopPressLeft()),
                new KeyFrame(Duration.seconds(dur += hoverDur), (e) -> transitionToQuiet())
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

    private void startPressRight() { bird.handlePressRight(); }

    private void stopPressRight() { bird.handleReleaseRight(); }

    private void startPressLeft() { bird.handlePressLeft(); }

    private void stopPressLeft() { bird.handleReleaseLeft(); }

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
