package com.juxtaflux.app;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LifetimeRect extends LifetimeActor {
    private Rectangle rect;
    private Pane parent;

    public LifetimeRect(Color color, double size, double x, double y, double lifetime, Pane parent) {
        super(lifetime);
        this.parent = parent;
        rect = new Rectangle(x, y, size, size);
        rect.setFill(color);
        parent.getChildren().add(rect);
    }

    @Override
    public void step(double delta) {
        super.step(delta);
    }

    @Override
    public void reapImpl() {
        parent.getChildren().remove(rect);
    }
}
