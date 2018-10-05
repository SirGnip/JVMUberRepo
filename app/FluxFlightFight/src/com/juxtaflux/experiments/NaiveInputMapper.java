package com.juxtaflux.experiments;

import com.juxtaflux.app.Bird;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/** A naive, direct input mapper that maps each key-down event into an explicit impulse (see {@link TraditionalInputMapper} for a more robust mapping) */
public class NaiveInputMapper implements InputMapper {
    private KitchenSinkExample game; // backpointer couples this class to main game class, but at last we have at least extracted input logic into this mapper.
    private Map<KeyCode, Runnable> keymap = new HashMap<>();
    private Map<MouseButton, Runnable> mousemap = new HashMap<>();

    NaiveInputMapper(KitchenSinkExample gameBackReference) {
        game = gameBackReference;
        checkNotNull(game);
        List<Bird> birds = game.getBirds();
        checkArgument(birds.size() == 4, "Expected 4 birds.");

        // Keyboard
        keymap.put(KeyCode.E, () -> {Bird b = birds.get(0); b.handleRight(); game.doBirdSound(b);});
        keymap.put(KeyCode.Q, () -> {Bird b = birds.get(0); b.handleLeft(); game.doBirdSound(b);});
        keymap.put(KeyCode.W, () -> {Bird b = birds.get(0); b.handleFlap(); game.doBirdSound(b);});
        keymap.put(KeyCode.RIGHT, () -> {Bird b = birds.get(1); b.handleRight(); game.doBirdSound(b);});
        keymap.put(KeyCode.LEFT, () -> {Bird b = birds.get(1); b.handleLeft(); game.doBirdSound(b);});
        keymap.put(KeyCode.UP, () -> {Bird b = birds.get(1); b.handleFlap(); game.doBirdSound(b);});
        keymap.put(KeyCode.B, () -> {Bird b = birds.get(2); b.handleRight(); game.doBirdSound(b);});
        keymap.put(KeyCode.C, () -> {Bird b = birds.get(2); b.handleLeft(); game.doBirdSound(b);});
        keymap.put(KeyCode.V, () -> {Bird b = birds.get(2); b.handleFlap(); game.doBirdSound(b);});
        keymap.put(KeyCode.P, () -> {Bird b = birds.get(3); b.handleRight(); game.doBirdSound(b);});
        keymap.put(KeyCode.I, () -> {Bird b = birds.get(3); b.handleLeft(); game.doBirdSound(b);});
        keymap.put(KeyCode.O, () -> {Bird b = birds.get(3); b.handleFlap(); game.doBirdSound(b);});

        // Mouse
        int BIRD_ID = 3;
        mousemap.put(MouseButton.PRIMARY, () -> {Bird b = birds.get(BIRD_ID); b.handleLeft(); game.doBirdSound(b);});
        mousemap.put(MouseButton.SECONDARY, () -> {Bird b = birds.get(BIRD_ID); b.handleRight(); game.doBirdSound(b);});
        mousemap.put(MouseButton.MIDDLE, () -> {Bird b = birds.get(BIRD_ID); b.handleFlap(); game.doBirdSound(b);});
    }

    public boolean handleKeyInput(KeyEvent e) {
        if (e.getEventType() != KeyEvent.KEY_PRESSED) {
            return false;
        }
        if (! keymap.containsKey(e.getCode())) {
            return false;
        }
        keymap.get(e.getCode()).run();
        return true;
    }

    public boolean handleMouseInput(MouseEvent e) {
        if (e.getEventType() != MouseEvent.MOUSE_PRESSED) {
            return false;
        }
        if (! mousemap.containsKey(e.getButton())) {
            return false;
        }
        mousemap.get(e.getButton()).run();
        return true;
    }
}
