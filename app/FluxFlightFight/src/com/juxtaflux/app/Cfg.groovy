package com.juxtaflux.app

/** Global constants */
class Cfg {
    static final boolean FULLSCREEN = false
    static final int GOAL_LEVEL = 500

    static class Scoreboard {
        static final int INITIAL_SCORE = 0
        static final int FONT_SIZE = 24
        static final int MAX_PLAYERS = 11
        static final int LABEL_X_START = 20
    }
    static class Audio {
        static final String DEATH = '/resources/audio/laser.wav'
        static final String FLAP1 = '/resources/audio/flap1.wav'
        static final String FLAP2 = '/resources/audio/flap2.wav'
        static final String FLAP3 = '/resources/audio/flap3.wav'
    }
}
