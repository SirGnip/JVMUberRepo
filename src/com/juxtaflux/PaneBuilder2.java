package com.juxtaflux;

import javafx.scene.layout.Pane;

/** Goal: Provide a GoovyFX-ish declarative "Builder" interface for grouping Controls under Panes.
Only partially successful. Is of limited use. Was a good learning experiment.

Warts:
- can't get easy access to the layout Panes to set their values (the argument passed into the lambda is base type of
"Pane", so it doesn't support the methods provided by concrete Panes, like "VBox.setSpacing()", etc.
- Would be nice to shorten up the "getChildren().add()" call as it is a bit verbose to call over and over.
 */
interface Builder2 {
    void build(Pane pane);
}
public class PaneBuilder2 {
    public static <T extends Pane> T make(T pane, Builder2 builder) {
        builder.build(pane);
        return pane;
    }
}
