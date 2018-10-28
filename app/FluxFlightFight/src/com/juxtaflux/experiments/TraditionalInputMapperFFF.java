package com.juxtaflux.experiments;

import com.juxtaflux.app.Bird;
import com.juxtaflux.app.FluxFlightFight;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/** A more traditional input schema that treats impulse as relative to the {@link }Bird}s current direction */
public class TraditionalInputMapperFFF implements InputMapper {
    FluxFlightFight game; // backpointer couples this class to main game class, but at last we have at least extracted input logic into this mapper.
    private Map<KeyCode, Runnable> keyPressMap = new HashMap<>();
    private Map<KeyCode, Runnable> keyReleaseMap = new HashMap<>();
    private Map<MouseButton, Runnable> mousePressMap = new HashMap<>();
    private Map<MouseButton, Runnable> mouseReleaseMap = new HashMap<>();

    TraditionalInputMapperFFF(FluxFlightFight gameBackReference) {
        game = gameBackReference;
        checkNotNull(game);
        List<Bird> birds = game.getBirds();
        checkArgument(birds.size() == 4, "Expected 4 birds.");

        // Keyboard
        keyPressMap.put(KeyCode.Q, () -> {Bird b = birds.get(0); b.handlePressLeft();});
        keyPressMap.put(KeyCode.W, () -> {Bird b = birds.get(0); b.handlePressRight();});
        keyPressMap.put(KeyCode.E, () -> {
            Bird b = birds.get(0);
            if (b.handlePressFlap()) {
                game.doBirdSound(b);
            }});
        keyReleaseMap.put(KeyCode.Q, () -> {Bird b = birds.get(0); b.handleReleaseLeft();});
        keyReleaseMap.put(KeyCode.W, () -> {Bird b = birds.get(0); b.handleReleaseRight();});
        keyReleaseMap.put(KeyCode.E, () -> {Bird b = birds.get(0); b.handleReleaseFlap();});

        keyPressMap.put(KeyCode.LEFT, () -> {Bird b = birds.get(1); b.handlePressLeft();});
        keyPressMap.put(KeyCode.DOWN, () -> {Bird b = birds.get(1); b.handlePressRight();});
        keyPressMap.put(KeyCode.RIGHT, () -> {
            Bird b = birds.get(1);
            if (b.handlePressFlap()) {
                game.doBirdSound(b);
            }});
        keyReleaseMap.put(KeyCode.LEFT, () -> {Bird b = birds.get(1); b.handleReleaseLeft();});
        keyReleaseMap.put(KeyCode.DOWN, () -> {Bird b = birds.get(1); b.handleReleaseRight();});
        keyReleaseMap.put(KeyCode.RIGHT, () -> {Bird b = birds.get(1); b.handleReleaseFlap();});

        keyPressMap.put(KeyCode.U, () -> {Bird b = birds.get(2); b.handlePressLeft();});
        keyPressMap.put(KeyCode.I, () -> {Bird b = birds.get(2); b.handlePressRight();});
        keyPressMap.put(KeyCode.O, () -> {
            Bird b = birds.get(2);
            if (b.handlePressFlap()) {
                game.doBirdSound(b);
            }});
        keyReleaseMap.put(KeyCode.U, () -> {Bird b = birds.get(2); b.handleReleaseLeft();});
        keyReleaseMap.put(KeyCode.I, () -> {Bird b = birds.get(2); b.handleReleaseRight();});
        keyReleaseMap.put(KeyCode.O, () -> {Bird b = birds.get(2); b.handleReleaseFlap();});

        keyPressMap.put(KeyCode.SEMICOLON, () -> {Bird b = birds.get(3); b.handlePressLeft();});
        keyPressMap.put(KeyCode.QUOTE, () -> {Bird b = birds.get(3); b.handlePressRight();});
        keyPressMap.put(KeyCode.ENTER, () -> {
            Bird b = birds.get(3);
            if (b.handlePressFlap()) {
                game.doBirdSound(b);
            }});
        keyReleaseMap.put(KeyCode.SEMICOLON, () -> {Bird b = birds.get(3); b.handleReleaseLeft();});
        keyReleaseMap.put(KeyCode.QUOTE, () -> {Bird b = birds.get(3); b.handleReleaseRight();});
        keyReleaseMap.put(KeyCode.ENTER, () -> {Bird b = birds.get(3); b.handleReleaseFlap();});

        // Mouse
        int BIRD_ID = 3;
        mousePressMap.put(MouseButton.PRIMARY, () -> {Bird b = birds.get(BIRD_ID); b.handlePressLeft();});
        mousePressMap.put(MouseButton.MIDDLE, () -> {Bird b = birds.get(BIRD_ID); b.handlePressRight();});
        mousePressMap.put(MouseButton.SECONDARY, () -> {
            Bird b = birds.get(BIRD_ID);
            if (b.handlePressFlap()) {
                game.doBirdSound(b);
            }});
        mouseReleaseMap.put(MouseButton.PRIMARY, () -> {Bird b = birds.get(BIRD_ID); b.handleReleaseLeft();});
        mouseReleaseMap.put(MouseButton.MIDDLE, () -> {Bird b = birds.get(BIRD_ID); b.handleReleaseRight();});
        mouseReleaseMap.put(MouseButton.SECONDARY, () -> {Bird b = birds.get(BIRD_ID); b.handleReleaseFlap();});
    }

    public boolean handleKeyInput(KeyEvent e) {
        if (e.getEventType() == KeyEvent.KEY_PRESSED) {
            if (! keyPressMap.containsKey(e.getCode())) {
                return false;
            }
            keyPressMap.get(e.getCode()).run();
            return true;
        } else if (e.getEventType() == KeyEvent.KEY_RELEASED) {
            if (! keyReleaseMap.containsKey(e.getCode())) {
                return false;
            }
            keyReleaseMap.get(e.getCode()).run();
            return true;
        }
        return false;
    }

    public boolean handleMouseInput(MouseEvent e) {
        if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {
            if (! mousePressMap.containsKey(e.getButton())) {
                return false;
            }
            mousePressMap.get(e.getButton()).run();
            return true;
        } else if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {
            if (! mouseReleaseMap.containsKey(e.getButton())) {
                return false;
            }
            mouseReleaseMap.get(e.getButton()).run();
            return true;
        }
        return false;
    }
}
