package com.juxtaflux.fluxlib;

import javafx.scene.Node;
import javafx.scene.Parent;

import java.util.Optional;

/**
There is not a generic method on Parent to allow for generic adding of
 children. (getChildren() is protected).
 */

public interface FXNodeMaker {
    Node makeGraphRoot();
}
