package com.juxtaflux.experiments;

import com.juxtaflux.fluxlib.FrameStepper;
import com.juxtaflux.fluxlib.Stepable;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import static com.juxtaflux.fluxlib.Flx.*;


/** Demonstrating features in Flx library */
public class FlxExample extends ExampleBase implements Stepable {
    private FrameStepper stepper;

    @Override
    public void buildRoot(Stage stage, Pane pane) {
        Pane grid = makeGrid(0, 0, 600, 600, 50, Color.DARKGRAY);
        Pane cross = makeCross(125,75, 100, Color.RED);
        Circle circle = new Circle(350, 100, 200, alphaize(Color.BLUE, 0.3));
        Line line = makeLine(0, 0, 500, 500, Color.YELLOW);

        pane.getChildren().addAll(grid, cross, circle, line);
        System.out.println("========== " + getClass().getSimpleName() + " scene graph");
        dump(pane);

        stepper = new FrameStepper(this).register();
    }

    @Override
    public void step(double delta) {
        if (stepper.getTotalFrameCount() % 200 == 0) {
            System.out.println(stepper.getFpsSummary());
        }
    }
}
