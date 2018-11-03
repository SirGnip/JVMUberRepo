package com.juxtaflux.app;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.function.Supplier;

/** Experiment to see if a class can use functional interfaces to define an Actor's look and behavior instead of inheritance or composition */
public class GenericLifetimeActor extends LifetimeActor {
    protected Node node;
    private Pane parent;

    public GenericLifetimeActor(double lifetime, Pane parent, Supplier<Node> nodeSupplier) {
        super(lifetime);
        node = nodeSupplier.get();
        this.parent = parent;
        parent.getChildren().add(node);
    }

    @Override
    public void reapImpl() {
        parent.getChildren().remove(node);
    }
}
