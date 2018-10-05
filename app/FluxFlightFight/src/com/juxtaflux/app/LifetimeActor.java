package com.juxtaflux.app;

import com.juxtaflux.fluxlib.Actor;

/** {@link Actor} that is reaped after its lifetime elapses */
public abstract class LifetimeActor implements Actor {
    protected double lifetime;

    LifetimeActor(double lifetime) {
        this.lifetime = lifetime;
    }

    @Override
    public void step(double delta) {
        lifetime -= delta;
    }

    @Override
    public boolean canReap() {
        return lifetime < 0;
    }
}
