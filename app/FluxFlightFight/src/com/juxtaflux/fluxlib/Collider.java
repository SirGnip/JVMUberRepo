package com.juxtaflux.fluxlib;

/*
Design summary:
Collider takes two Collidables, queries their Bounds and determines if they collided (position,
stage of actor, etc). If they collided, call the onCollided() method of both Collidables.  There are multiple types
of collision and Collidables should be able to respond differently depending on the type of collision.

Design goal: Keep collision logic outside of actors. as long as an object exposes a "getBounds()"
they can participate in collisions. This allows objects of very different types to collide. Also allows me to reuse
common collision code by creating a reusable class.

Questions:
- Maybe Collider is just a static method?
- Do I support one-sided colliders?  Where only one object gets notified of the collision (based on logic in the collision?)
- Instead of having all colliders call through some Collidable.onCollided() method (which means all Colliders will have to put implmeentations
for all types of collisions in this one method.  Can Collidables register a callback? Provide a lambda?

Naming:
- Collider and Collideable?  Touchable and Toucher?

General design for a Collider:
- general design - separation of responsibilities:
    - collision geometry
    - detection of collisions
    - responses taken by objects
- general design restated:
    - objects know their geometry and how to respond to different types of collision
    - collider knows if things touch, but it has no knowledge of how things react in response
- details
    - each object reports its simplified collision geometry (box? line? circle?)
    - collider knows how to collide simplified collision geometry
    - if a collision takes place, it sends a message to each object involved in the collision
        - the objects may want to get some details about the collision in the message that they are passed (other object, contact side, contact location, maybe some helper methods that calculate vectors and offsets, etc)
- thoughts
    - may want to have a "BoxCollider" a "CircleCollider", etc. to specialize collisions
    - order of which object gets the collision event first may be significant
    - instead of calling methods on the objects that collide, can you do some kind of lambda/callback instead to parameterize the behavior resulting from a collision?


Oversimplified example:

    interface Collidable
        getBounds()
        onCollided(Collider collider, Collidable other)

    random snippet...
        bool collisionObj = Collider.testForCollision(collidable1, collideable2)
        if (collisionObj.hasCollided()) {
            collidable1.hasCollided(Collider.getClass(), collidable2, collisionObj);
            collidable2.hasCollided(Collider.getClass(), collidable1, collisionObj);
        }


*/


// - In current state of code, derive two colliders, one is a simple "are they touching?" collider and the other is the "joustedHigher" collider.
public class Collider {
}
