package com.juxtaflux.experiments;

import com.juxtaflux.app.FluxFlightFight;
import javafx.application.Application;
import javafx.stage.Stage;

/** Run all examples in sequence */
public class AllExamplesRunner extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        new FlxExample().build().showAndWait();
        new GlowExperiment().build().showAndWait();
        new JInputDump().build().showAndWait();
        new AudioPlaybackTest().build().showAndWait();
        new KitchenSinkExample().build().showAndWait();
        new FluxFlightFight().build().showAndWait();
        System.out.println(getClass().getSimpleName() + " is DONE");
    }
}
