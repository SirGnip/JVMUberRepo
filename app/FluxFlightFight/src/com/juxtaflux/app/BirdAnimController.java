package com.juxtaflux.app;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/** An animation-based controller for a bird */
public class BirdAnimController {

    public BirdAnimController(Bird bird) {
        Timeline timeline = new Timeline();
        // accelerate
        for (double t = 3.0; t < 3.7; t += 0.1) {
            timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(t), (e) -> bird.handlePressFlap()),
                new KeyFrame(Duration.seconds(t + 0.01), (e) -> bird.handleReleaseFlap())
            );
        }
        // decelerate
        for (double t = 5.0; t < 5.1; t += 0.05) {
            timeline.getKeyFrames().addAll(
                    new KeyFrame(Duration.seconds(t), (e) -> bird.handlePressFlap()),
                    new KeyFrame(Duration.seconds(t + 0.01), (e) -> bird.handleReleaseFlap())
            );
        }

        // hover
        for (double t = 5.2; t < 15.0; t += 0.258) { // .261 falls,
            timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(t), (e) -> bird.handlePressFlap()),
                new KeyFrame(Duration.seconds(t + 0.01), (e) -> bird.handleReleaseFlap())
            );
        }
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
