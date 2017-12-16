package com.juxtaflux.experiments;

import javafx.scene.input.KeyEvent;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import static com.google.common.base.Preconditions.checkNotNull;

/** A more traditional input schema that treats impulse as relative to the Birds current direction */
public class TraditionalInputMapper implements InputMapper {
    KitchenSinkExample game; // backpointer couples this class to main game class, but at last we have at least extracted input logic into this mapper.

    TraditionalInputMapper(KitchenSinkExample gameBackReference) {
        game = gameBackReference;
        checkNotNull(game);
    }

    public boolean handleKeyInput(KeyEvent e) {
        game.getBirds().get(0).handleFlap();
        return true;
    }
}
