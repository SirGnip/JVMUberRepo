package com.juxtaflux.experiments;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/** Helper class to reduce boiler plate for quick example apps and allows
 * multiple example apps to be opened up in sequence programmatically

Goal: is to be able to quickly cycle through a bunch of test apps as a part of "manual testing"
- reduce boilerplate code for example apps
- allow example app to be build individually in isolation (for individual debugging, etc.)
- allow example apps to be "composable" so that other code can:
- open up one example app after another in sequence, blocking until each window is closed
- open a number of windows all at once

Ex:
    new MyWin().build().showAndWait();
    new MyWin2().build().showAndWait();
 */
public abstract class ExampleBase extends Application {
    protected int width = 1280;
    protected int height = 720;
    private Color bgColor = Color.BLACK;
    Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        sharedBuild(stage).show();
    }

    /** programmatic way to run this class, call .show() or showAndWait() on returned Stage */
    public Stage build() throws Exception {
        return sharedBuild(new Stage());
    }

    private Stage sharedBuild(Stage stage) {
        this.stage = stage;
        Pane pane = new Pane();
        Scene scene = new Scene(pane, width, height, bgColor);
        stage.setScene(scene);
        stage.setTitle(getClass().getSimpleName());
        buildRoot(stage, pane);
        return stage;
    }

    protected void close() {
        stage.fireEvent(
                new WindowEvent(
                        stage,
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        );
    }

    /** Subclass code should override this, generate a scene graph, and return the root */
    protected abstract void buildRoot(Stage stage, Pane pane);
}
