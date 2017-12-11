package com.juxtaflux.fluxlib;

public interface Stepable {
    /** Advance simulation by "delta" seconds */
    void step(double delta);
}
