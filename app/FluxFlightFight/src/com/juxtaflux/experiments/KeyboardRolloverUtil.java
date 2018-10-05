package com.juxtaflux.experiments;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/** A util app to help determine interactively what keys are part of keyboard rollover groups.
Process: Press and hold the multiple keys you are interested in. If you hear a beep (this comes
from the OS), that means too many keys are pressed.  Keep experimenting to find the key
combinations that work with each other.

In general, can press any 2 keys simultaneously in each group listed below. Pressing a 3rd key will
jam. However, you can press 3-4 keys if they are all in the same vertical row (ex: 1QAZ, 2WSX).
Can also get 4 to work if staggered in zig-zag fashion (2QSZ).  It appears that keys in a right-angle
shape are prone to jamming.

~12
qw
as
zx

345
ert
dfg
cv

Combos that let me do 9 keys at once:
234 jkl left/down/right
123 jkl left/down/right

I was able to find 10-key combos.

Note: Can't use right-shift as if you hold it down for 8-ish seconds, "Sticky Keys" will popup.
*/
public class KeyboardRolloverUtil extends ExampleBase {
    private boolean[] state = new boolean[256];

    public void dumpState() {
        int ct = 0;
        String msg = "";
        for (int i = 0; i < state.length; ++i) {
            if (state[i]) {
                ct++;
                msg += String.format("%-12s", KeyCode.values()[i].getName()); // print key names
            }
        }
        System.out.println(ct + "|" + msg);
    }
    private void handleEvt(KeyEvent e) {
        if (e.getEventType() == KeyEvent.KEY_TYPED) { return; }

        if (e.getEventType() == KeyEvent.KEY_PRESSED && e.getCode().equals(KeyCode.ESCAPE)) {
            close();
        }
        if (e.getEventType() == KeyEvent.KEY_PRESSED) {
            state[e.getCode().ordinal()] = true;
            dumpState();
        } else if (e.getEventType() == KeyEvent.KEY_RELEASED) {
            state[e.getCode().ordinal()] = false;
            dumpState();
        }
    }

    @Override
    protected void buildRoot(Stage stage, Pane pane) {
        for (int i = 0; i < state.length; ++i) {
            state[i] = false;
        }
        stage.getScene().setOnKeyPressed(this::handleEvt);
        stage.getScene().setOnKeyTyped(this::handleEvt);
        stage.getScene().setOnKeyReleased(this::handleEvt);
    }
}
