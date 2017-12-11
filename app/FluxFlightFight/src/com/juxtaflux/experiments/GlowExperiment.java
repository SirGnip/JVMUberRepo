package com.juxtaflux.experiments;

import javafx.scene.Parent;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/** Experimenting with glow effects */
public class GlowExperiment extends ExampleBase {
    Color alphaIt(Color clr, double opacity) {
        return new Color(clr.getRed(), clr.getGreen(), clr.getBlue(), opacity);
    }

    @Override
    public void buildRoot(Stage stage, Pane pane) {
        double alpha = 0.8;
        BoxBlur boxBlur = new BoxBlur();
        boxBlur.setWidth(10);
        boxBlur.setHeight(10);
        boxBlur.setIterations(3);
        Effect fx = boxBlur;
        boxBlur.setInput(new Glow(1.0));

//        Effect fx = new Glow(1.0);
//        Effect fx = new Bloom(1.0);

//        Glow glow = new Glow(1.0);
//        glow.setInput(new Glow(1.0));
//        Effect fx = glow;

//        Bloom bloom = new Bloom(1.0);
//        bloom.setInput(new Bloom(1.0));
//        Effect fx = bloom;

//        Glow gl1 = new Glow(1.0);
//        Glow gl2 = new Glow(1.0);
//        Glow gl3 = new Glow(1.0);
//        Glow gl4 = new Glow(1.0);
//        Glow gl5 = new Glow(1.0);
//        gl1.setInput(gl2);
//        gl2.setInput(gl3);
//        gl3.setInput(gl4);
//        gl4.setInput(gl5);
//        Effect fx = gl1;

//        root.setEffect(fx);
        Line line = new Line(50, 100, 350, 100);
        line.setStroke(alphaIt(Color.RED, alpha));
        line.setStrokeWidth(5);
        line.setEffect(fx);

        Line lineb = new Line(50, 100, 350, 100);
        lineb.setStroke(alphaIt(Color.RED, alpha));
        lineb.setStrokeWidth(2);

        CubicCurve cubic = new CubicCurve(0, 300, 110, -125, 300, 130, 350, 300);
        cubic.setStroke(alphaIt(Color.YELLOW, alpha));
        cubic.setStrokeWidth(5);
        cubic.setFill(Color.TRANSPARENT);
        cubic.setEffect(fx);

        CubicCurve cubicb = new CubicCurve(0, 300, 110, -125, 300, 130, 350, 300);
        cubicb.setStroke(alphaIt(Color.YELLOW, alpha));
        cubicb.setStrokeWidth(2);
        cubicb.setFill(Color.TRANSPARENT);

        CubicCurve cubic2 = new CubicCurve(0, 0, 400, 300, 100, 300, 50, 300);
        cubic2.setStroke(alphaIt(Color.BLUE, alpha));
        cubic2.setStrokeWidth(5);
        cubic2.setFill(Color.TRANSPARENT);
        cubic2.setEffect(fx);

        CubicCurve cubic2b = new CubicCurve(0, 0, 400, 300, 100, 300, 50, 300);
        cubic2b.setStroke(alphaIt(Color.BLUE, alpha));
        cubic2b.setStrokeWidth(2);
        cubic2b.setFill(Color.TRANSPARENT);

//        root.getChildren().add(cubic);
        pane.getChildren().addAll(line, lineb, cubic, cubicb, cubic2, cubic2b);
    }
}
