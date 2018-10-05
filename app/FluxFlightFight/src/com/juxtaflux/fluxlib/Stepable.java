package com.juxtaflux.fluxlib;

/** Control the incremental advance of simulation */
public interface Stepable {
    /** Advance simulation by "delta" seconds */
    void step(double delta);
}
