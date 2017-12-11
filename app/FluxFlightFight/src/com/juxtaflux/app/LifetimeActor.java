package com.juxtaflux.app;

import com.juxtaflux.fluxlib.Actor;

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
