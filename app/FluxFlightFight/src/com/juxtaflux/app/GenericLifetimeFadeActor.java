package com.juxtaflux.app;

import com.juxtaflux.fluxlib.Flx;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.function.Supplier;

// TODO: May be able to do this with a FadeTransition

public class GenericLifetimeFadeActor extends GenericLifetimeActor {
    public GenericLifetimeFadeActor(double lifetime, Pane parent, Supplier<Node> nodeSupplier) {
        super(lifetime, parent, nodeSupplier);
    }

    @Override
    public void step(double delta) {
        super.step(delta);
        node.setOpacity(Flx.normalize(lifetime, 0, lifetimeStart));
    }
}
