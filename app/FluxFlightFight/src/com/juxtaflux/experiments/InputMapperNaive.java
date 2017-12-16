package com.juxtaflux.experiments;

import com.juxtaflux.app.Bird;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/** A naive, direct input mapper that maps each key-down event into an explicit impulse */
public class InputMapperNaive {
    KitchenSinkExample game; // backpointer couples this class to main game class, but at last we have at least extracted input logic into this mapper.
    Map<KeyCode, Pair<Bird, Vector2D>> keymap = new HashMap<>();

    InputMapperNaive(KitchenSinkExample gameBackReference) {
        game = gameBackReference;
        checkNotNull(game);
        checkArgument(game.getBirds().size() == 4, "Expected 4 birds.");
        double vertImpulse = 200.0;
        double horizImpulse = 75.0;

        // TODO: move all this impulse vector into Bird. That seems to be where it belongs.
        Vector2D impulseRight = new Vector2D(horizImpulse, 0);
        Vector2D impulseLeft = new Vector2D(-horizImpulse, 0);
        Vector2D impulseUp = new Vector2D(0, -vertImpulse);
        keymap.put(KeyCode.E, new Pair<>(game.getBirds().get(0), impulseRight));
        keymap.put(KeyCode.Q, new Pair<>(game.getBirds().get(0), impulseLeft));
        keymap.put(KeyCode.W, new Pair<>(game.getBirds().get(0), impulseUp));
        keymap.put(KeyCode.RIGHT, new Pair<>(game.getBirds().get(1), impulseRight));
        keymap.put(KeyCode.LEFT, new Pair<>(game.getBirds().get(1), impulseLeft));
        keymap.put(KeyCode.UP, new Pair<>(game.getBirds().get(1), impulseUp));
        keymap.put(KeyCode.B, new Pair<>(game.getBirds().get(2), impulseRight));
        keymap.put(KeyCode.C, new Pair<>(game.getBirds().get(2), impulseLeft));
        keymap.put(KeyCode.V, new Pair<>(game.getBirds().get(2), impulseUp));
        keymap.put(KeyCode.P, new Pair<>(game.getBirds().get(3), impulseRight));
        keymap.put(KeyCode.I, new Pair<>(game.getBirds().get(3), impulseLeft));
        keymap.put(KeyCode.O, new Pair<>(game.getBirds().get(3), impulseUp));
    }

    public boolean handleKeyInput(KeyEvent e) {
        if (! keymap.containsKey(e.getCode())) {
            return false;
        }
        Pair<Bird, Vector2D> pair = keymap.get(e.getCode());
        game.handleInput(pair.getKey(), pair.getValue());
        return true;
    }

    public boolean handleMouseInput(MouseEvent e) {
        int MOUSE_BIRD_ID = 3;
        double vertImpulse = 200.0;
        double horizImpulse = 75.0;
        Vector2D impulseRight = new Vector2D(horizImpulse, 0);
        Vector2D impulseLeft = new Vector2D(-horizImpulse, 0);
        Vector2D impulseUp = new Vector2D(0, -vertImpulse);
        Bird b = game.getBirds().get(MOUSE_BIRD_ID);
        if (e.getButton().equals(MouseButton.PRIMARY) ) {
           game.handleInput(b, impulseLeft);
           return true;
        } else if (e.getButton().equals(MouseButton.SECONDARY)) {
            game.handleInput(b, impulseRight);
            return true;
        } else if (e.getButton().equals(MouseButton.MIDDLE)) {
            game.handleInput(b, impulseUp);
            return true;
        }
        return false;
    }
}
