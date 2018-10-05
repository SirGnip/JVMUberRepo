package com.juxtaflux.fluxlib;

import java.util.ArrayList;

/** Container that automatically steps and manages the lifetime of {@link Actor}s */
/* POTENTIAL PROBLEM: Since the ArrayList is <Actor>, if I try to pull an object out of the list to
 do something with it, I can only treat it as an Actor. I might want to make ActorList a generic so I
can have a ActorList<Particle>, ActorList<Player>, etc. and be able to do Particle and Player type
things to the individual elements.
*/
public class ActorList implements Actor {
    public ArrayList<Actor> actors = new ArrayList<>();

    @Override
    public void step(double delta) {
        // TODO: I'm sure there is some method/stream that lets you remove items that match a predicate
        ArrayList<Actor> actorsToReap = new ArrayList<>();
        for (Actor actor : actors) {
            actor.step(delta);
            if (actor.canReap()) {
                actorsToReap.add(actor);
                actor.reapImpl();
            }
        }
        actors.removeAll(actorsToReap);
    }

    @Override
    public boolean canReap() {
        return actors.size() == 0;
    }

    @Override
    public void reapImpl() {
    }
}
