package com.juxtaflux.experiments;

import javafx.application.Application;
import javafx.stage.Stage;

/** Run all examples in sequence */
public class AllExamplesRunner extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        new FlxExample().build().showAndWait();
        new GlowExperiment().build().showAndWait();
        new KitchenSinkExample().build().showAndWait();
        System.out.println(getClass().getSimpleName() + " is DONE");
    }
}
