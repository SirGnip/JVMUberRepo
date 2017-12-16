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
public class NaiveInputMapper implements InputMapper {
    KitchenSinkExample game; // backpointer couples this class to main game class, but at last we have at least extracted input logic into this mapper.
    Map<KeyCode, Pair<Bird, Vector2D>> keymap = new HashMap<>();
    private double VERT_IMPULSE = 200.0;
    private double HORIZ_IMPULSE = 75.0;
    private Vector2D RIGHT_IMPULSE = new Vector2D(HORIZ_IMPULSE, 0);
    private Vector2D LEFT_IMPULSE = new Vector2D(-HORIZ_IMPULSE, 0);
    private Vector2D UP_IMPULSE = new Vector2D(0, -VERT_IMPULSE);

    NaiveInputMapper(KitchenSinkExample gameBackReference) {
        game = gameBackReference;
        checkNotNull(game);
        checkArgument(game.getBirds().size() == 4, "Expected 4 birds.");

        // TODO: move all this impulse vector into Bird. That seems to be where it belongs.
        keymap.put(KeyCode.E, new Pair<>(game.getBirds().get(0), RIGHT_IMPULSE));
        keymap.put(KeyCode.Q, new Pair<>(game.getBirds().get(0), LEFT_IMPULSE));
        keymap.put(KeyCode.W, new Pair<>(game.getBirds().get(0), UP_IMPULSE));
        keymap.put(KeyCode.RIGHT, new Pair<>(game.getBirds().get(1), RIGHT_IMPULSE));
        keymap.put(KeyCode.LEFT, new Pair<>(game.getBirds().get(1), LEFT_IMPULSE));
        keymap.put(KeyCode.UP, new Pair<>(game.getBirds().get(1), UP_IMPULSE));
        keymap.put(KeyCode.B, new Pair<>(game.getBirds().get(2), RIGHT_IMPULSE));
        keymap.put(KeyCode.C, new Pair<>(game.getBirds().get(2), LEFT_IMPULSE));
        keymap.put(KeyCode.V, new Pair<>(game.getBirds().get(2), UP_IMPULSE));
        keymap.put(KeyCode.P, new Pair<>(game.getBirds().get(3), RIGHT_IMPULSE));
        keymap.put(KeyCode.I, new Pair<>(game.getBirds().get(3), LEFT_IMPULSE));
        keymap.put(KeyCode.O, new Pair<>(game.getBirds().get(3), UP_IMPULSE));
    }

    public boolean handleInput(KeyEvent e) {
        if (! keymap.containsKey(e.getCode())) {
            return false;
        }
        Pair<Bird, Vector2D> pair = keymap.get(e.getCode());
        game.handleInput(pair.getKey(), pair.getValue());
        return true;
    }

    public boolean handleInput(MouseEvent e) {
        int MOUSE_BIRD_ID = 3;
        Bird b = game.getBirds().get(MOUSE_BIRD_ID);
        if (e.getButton().equals(MouseButton.PRIMARY) ) {
           game.handleInput(b, LEFT_IMPULSE);
           return true;
        } else if (e.getButton().equals(MouseButton.SECONDARY)) {
            game.handleInput(b, RIGHT_IMPULSE);
            return true;
        } else if (e.getButton().equals(MouseButton.MIDDLE)) {
            game.handleInput(b, UP_IMPULSE);
            return true;
        }
        return false;
    }
}
