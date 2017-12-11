package com.juxtaflux.fluxlib;

/** Control the end of an object's lifetime */
/*
Do I want the actor to get notified when it gets reaped?
- or do i just add something in the "canReap()" overload?
*/
public interface Reapable {
    /** Query to determine if Reapable is ready to be reaped */
    boolean canReap();
    /** Where a Reapable performs the implementation of Reaping itself */
    void reapImpl();
}
