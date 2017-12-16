package com.juxtaflux.experiments;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public interface InputMapper {
    default boolean handleInput(KeyEvent e) {
        return false;
    }
    default boolean handleInput(MouseEvent e) {
        return false;
    }
}