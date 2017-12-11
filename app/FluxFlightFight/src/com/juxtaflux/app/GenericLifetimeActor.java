package com.juxtaflux.app;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.function.Supplier;

/** An experimental class to see how functional interfaces can be used to make a very generic class */
public class GenericLifetimeActor extends LifetimeActor {
    private Node node;
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
