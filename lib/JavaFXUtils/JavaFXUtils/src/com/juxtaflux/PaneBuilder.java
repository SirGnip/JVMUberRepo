package com.juxtaflux;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import java.util.List;

/** Goal: Provide a GoovyFX-ish declarative "Builder" interface for grouping Controls under Panes.
Only partially successful. Is of limited use. Was a good learning experiment.

Warts:
- Can't reuse variable names in nested scope (limitation of lambda syntax)
- Have to return children as an array in the lambda so that they get added to the Pane. Awkward syntax.
- Could pass a context object of some sort into the lambda, but then I run into more nested name collisions.
*/

interface Builder {
    List<Region> build();
}

public class PaneBuilder<T extends Pane> {
    public T pane;
    PaneBuilder(T thePane, Builder builder) {
        pane = thePane;
        List<Region> children = builder.build();
        for (Region r : children) {
            pane.getChildren().add(r);
        }
    }
}
