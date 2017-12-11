package com.juxtaflux.fluxlib;

import org.junit.Test;

import static org.junit.Assert.*;

class TestActor implements Actor {
    public int stepCount = 0;

    @Override
    public void step(double delta) {
        ++stepCount;
    }

    @Override
    public boolean canReap() {
        return stepCount >= 3;
    }

    @Override
    public void reapImpl() {
    }
}

public class ActorListTest {
    @Test
    public void smokeTestActorListStepableReapable() {
        Actor a1 = new TestActor();
        Actor a2 = new TestActor();
        ActorList actorList = new ActorList();
        assertEquals(0, actorList.actors.size());

        actorList.actors.add(a1);
        actorList.step(0.1);
        actorList.actors.add(a2);
        assertEquals(2, actorList.actors.size());
        assertFalse(actorList.canReap());

        actorList.step(0.1);
        actorList.step(0.1);
        assertTrue(a1.canReap());
        assertFalse(a2.canReap());
        assertEquals(1, actorList.actors.size());
        assertFalse(actorList.canReap());

        actorList.step(0.1);
        assertTrue(a2.canReap());
        assertTrue(actorList.canReap());
        assertTrue(actorList.actors.isEmpty());
    }
}