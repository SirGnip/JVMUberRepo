package com.juxtaflux.experiments;

import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/** Helper class to reduce boiler plate for quick JavaFX example apps and allows multiple example apps to be opened up in sequence programmatically.
 <p>
Goal: able to quickly create and cycle through a bunch of minimal test apps as a part of "manual testing":
<ul>
<li> reduce boilerplate code for example apps
<li> allow example app to be built individually in isolation (for individual debugging, etc.)
<li> allow example apps to be "composable" so that the top-level "runner" code can either:
    <ul>
    <li> open up one example app after another in sequence, blocking until each window is closed
    <li> open a number of windows all at once
    </ul>
 </ul>
 <p>
Ex:
 <pre>{@code
    new MyWin().build().showAndWait();
    new MyWin2().build().showAndWait();
 }</pre>
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
        Stage newStage = new Stage();
        sharedBuild(newStage);
        newStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::onExit);
        return newStage;
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

    /** Subclass code should override this, doing all its work before calling super.onExit(t) */
    public <T extends Event> void onExit(T event) {
    }

    /** Subclass code should override this, generate a scene graph, and return the root */
    protected abstract void buildRoot(Stage stage, Pane pane);
}
