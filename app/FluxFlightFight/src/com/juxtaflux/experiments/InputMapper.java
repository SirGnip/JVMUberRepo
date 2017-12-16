package com.juxtaflux.experiments;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public interface InputMapper {
    default boolean handleKeyInput(KeyEvent e) {
        return false;
    }
    default boolean handleMouseInput(MouseEvent e) {
        return false;
    }
}