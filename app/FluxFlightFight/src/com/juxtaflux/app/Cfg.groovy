package com.juxtaflux.app

/** Global constants */
class Cfg {
    static final boolean FULLSCREEN = false
    static final int GOAL_LEVEL = 200

    static class Scoreboard {
        static final int INITIAL_SCORE = 0
        static final int FONT_SIZE = 24
        static final int MAX_PLAYERS = 11
        static final int LABEL_X_START = 20
    }
    static class Audio {
        static final String DEATH = '/com/juxtaflux/standInResources/audio/voice/crash.wav'
        static final String FLAP1 = '/com/juxtaflux/standInResources/audio/voice/click1.wav'
        static final String FLAP2 = '/com/juxtaflux/standInResources/audio/voice/click2.wav'
        static final String FLAP3 = '/com/juxtaflux/standInResources/audio/voice/click3.wav'
    }
}
