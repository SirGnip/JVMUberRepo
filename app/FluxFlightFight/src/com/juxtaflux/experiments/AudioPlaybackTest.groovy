package com.juxtaflux.experiments

import javafx.scene.input.KeyCode
import javafx.scene.layout.Pane
import javafx.scene.media.AudioClip
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.stage.Stage

class AudioPlaybackTest extends ExampleBase {
    @Override
    void buildRoot(Stage stage, Pane pane) {
        def soundMap = [
                (KeyCode.DIGIT1): new AudioClip(getClass().getResource("/com/juxtaflux/standInResources/audio/voice/buzz.wav").toString()),
                (KeyCode.DIGIT2): new AudioClip(getClass().getResource("/com/juxtaflux/standInResources/audio/voice/click1.wav").toString()),
                (KeyCode.DIGIT3): new AudioClip(getClass().getResource("/com/juxtaflux/standInResources/audio/voice/click2.wav").toString()),
                (KeyCode.DIGIT4): new AudioClip(getClass().getResource("/com/juxtaflux/standInResources/audio/voice/click3.wav").toString()),
                (KeyCode.DIGIT5): new AudioClip(getClass().getResource("/com/juxtaflux/standInResources/audio/voice/click4.wav").toString()),
                (KeyCode.DIGIT6): new AudioClip(getClass().getResource("/com/juxtaflux/standInResources/audio/voice/crash.wav").toString()),
                (KeyCode.DIGIT7): new AudioClip(getClass().getResource("/com/juxtaflux/standInResources/audio/voice/ding.wav").toString()),
                (KeyCode.DIGIT8): new AudioClip(getClass().getResource("/com/juxtaflux/standInResources/audio/voice/splat.wav").toString())
        ]

        def txt = new Text(20, 50,"Press the number keys to trigger sounds")
        txt.setFont(new Font(36))
        txt.setFill(Color.BLUE)
        pane.getChildren().add(txt)
        stage.getScene().setOnKeyPressed { e ->
            if (soundMap.containsKey(e.getCode())) {
                println e
                soundMap[e.getCode()].play()
            }
        }
    }
}
