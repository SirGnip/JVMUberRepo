package com.juxtaflux.app;

import com.juxtaflux.fluxlib.Flx;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/** Sample particle with color, size, lifetime */
public class SimpleParticle extends LifetimeActor {
    private Circle circle;
    private Pane parent;

    public SimpleParticle(Color color, double particleSize, double x, double y, double lifetime, Pane parent) {
        super(lifetime);
        this.parent = parent;
        circle = new Circle(x, y, particleSize, color);
        circle.setFill(color);

        TranslateTransition trans = new TranslateTransition(Duration.seconds(lifetime), circle);
        trans.setByX(Flx.rnd() * 100.0 - 50.0);
        trans.setByY(Flx.rnd() * 100.0 - 50.0);
        trans.play();

        parent.getChildren().add(circle);
    }

    @Override
    public void step(double delta) {
        super.step(delta);
    }

    @Override
    public void reapImpl() {
        parent.getChildren().remove(circle);
    }
}
