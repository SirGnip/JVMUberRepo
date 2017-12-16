package com.juxtaflux.experiments;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/** A naive, direct input mapper that maps each key-down event into an explicit impulse */
public class NaiveInputMapper implements InputMapper {
    private KitchenSinkExample game; // backpointer couples this class to main game class, but at last we have at least extracted input logic into this mapper.
    private Map<KeyCode, Runnable> keymap = new HashMap<>();
    private Map<MouseButton, Runnable> mousemap = new HashMap<>();

    NaiveInputMapper(KitchenSinkExample gameBackReference) {
        game = gameBackReference;
        checkNotNull(game);
        checkArgument(game.getBirds().size() == 4, "Expected 4 birds.");

        // Keyboard
        keymap.put(KeyCode.E, game.getBirds().get(0)::handleRight);
        keymap.put(KeyCode.Q, game.getBirds().get(0)::handleLeft);
        keymap.put(KeyCode.W, game.getBirds().get(0)::handleFlap);
        keymap.put(KeyCode.RIGHT, game.getBirds().get(1)::handleRight);
        keymap.put(KeyCode.LEFT, game.getBirds().get(1)::handleLeft);
        keymap.put(KeyCode.UP, game.getBirds().get(1)::handleFlap);
        keymap.put(KeyCode.B, game.getBirds().get(2)::handleRight);
        keymap.put(KeyCode.C, game.getBirds().get(2)::handleLeft);
        keymap.put(KeyCode.V, game.getBirds().get(2)::handleFlap);
        keymap.put(KeyCode.P, game.getBirds().get(3)::handleRight);
        keymap.put(KeyCode.I, game.getBirds().get(3)::handleLeft);
        keymap.put(KeyCode.O, game.getBirds().get(3)::handleFlap);

        // Mouse
        int BIRD_ID = 3;
        mousemap.put(MouseButton.PRIMARY, game.getBirds().get(BIRD_ID)::handleRight);
        mousemap.put(MouseButton.SECONDARY, game.getBirds().get(BIRD_ID)::handleLeft);
        mousemap.put(MouseButton.MIDDLE, game.getBirds().get(BIRD_ID)::handleFlap);
    }

    public boolean handleKeyInput(KeyEvent e) {
        if (! keymap.containsKey(e.getCode())) {
            return false;
        }
        keymap.get(e.getCode()).run();
        return true;
    }

    public boolean handleMouseInput(MouseEvent e) {
        if (! mousemap.containsKey(e.getButton())) {
            return false;
        }
        mousemap.get(e.getButton()).run();
        return true;
    }
}
