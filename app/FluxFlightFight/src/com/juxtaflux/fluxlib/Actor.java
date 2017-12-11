package com.juxtaflux.fluxlib;

/** Object that can be stepped and reaped */
/*
Problem: It is difficult to handle the adding of an Actor's scene graph nodes to the main JavaFX scene graph because I can't store and use a reference to Parent and call getChildren() because it is protected on Parent. But, it is public on its subclasses (Group and Pane).  Possible solutions:

- Generics (but still won't work because Parent is the base class and if I use <P extends Pane>, I still can't call P.getChildren() because the compiler requires that P be a Parent or sublcass of Parent, meaning getChildren() is seen is protected.
- could do runtime type testing and downcasting. Ugly, yes.
- Maybe pass some kind of lambdas to Actor that do the work of parenting and unparenting as a way around the type system?
- Maybe have different classes that handle the two different subclass trees under Parent that make getChildren() public (Group and Pane)
- Might also be able to implement ActorList as a subclass of Pane and/or Group?

# Design Attempt #1

Maybe don't need an "Actor" class. Just have individual interfaces that I can bolt on to anything?

For the "SceneGraphable" interface, have the "makeGraph" take Optional<Parent>. That way, it can automatically parent it to the scene graph AND maintain a reference tot the parent so that when it is reaped it can call this.parent.remove(this) to handle cleaning up.


What Actors can do:
- SceneGraphAble
    - name: FXNodeable?, FXNodeMaker? SceneGraphMaker? GraphMaker? FXGraphMaker?
- Stepable
- Drawable
- Reapable


Lifecycle on Actors

- they are created
    - set initial state (use lambdas for flexibility?)
    - assemble its scene graph elements
    - parented to some parent external to itself
- they live and do their stuff
    - drawn
    - stepped externally (for simulation)
    - interact (collide, interact send/receive messages)
    - something needs to have a reference to it
- they die
    - initiated by:
        - internal simulation (lifetime timer, response to events)
        - external (told to die by something external)
    - removed from parent
        - death initiated internally: have to remove itself from parent
        - death initiated externally: the external thing can remove Actor from its parent?

Other stuff
- Emitters are objects that create Actors (according to rules) and set their initial state
- "Cloud" is a grouping of Actors that can be manipulated (transforms, lifecycle, etc) as a group
    - Emitters can probably be coupled with a Cloud.
    - Maybe "Cloud" is just an ActorList. Or, SteppableList. Or SteppableReapableList.
- any way to keep this code separate from explicit references to JavaFX "Group", "Node", "Parent" so that I can use these interfaces/classes with both JavaFX scene graph and JavaFX canvas?


# Design Attempt #2

Client code

    Bird bird = new Bird(yellow)
    Node birdNode = bird.makeGraphRoot()
    sceneGraph.getChildren().add(birdNode);
    actorList.add(bird);
    ...
    actorList.step(delta);
    ...
    # logic for reaping happens in actorList.step(). "if actor.canReap() list.remove(actor)"

    # when reaped, bird must be removed from ActorList and sceneGraph

    can Actor have reference to Pane, assuming that is where it will be grouped under?
    - if i can make this assumption, i can have Actor store ref to Pane and do removal itself
    - problem: Actor needs to know how to call getChildren().remove() from any object in the scene graph.
    If I can't assume this, it means that I could maybe pass in a lambda that does the deregistration work, which could be much more flexible.


# Design Attempt #3

Use cases to throw at my design

- create Actor and attach visuals to scene graph
- remove Actor from ActorList AND scene graph when reaped
- can ActorList be treated like an Actor and composed?
- do scene graph pieces get built and parented to scene graph during Actor ctor or lazily when FXNodeMaker.makeGraphRoot() is called? Up to class impl i guess
- can I use Actor, ActorList in something other than JavaFX?
- what if scene graph items are parented to something other than a Pane?
- options for handling scene graph
    - pass Pane to actor ctor, create graph, in ctor call Pane.getChildren().add(myRoot). have parent for reaping
    - Have FXNodeActor be a generic where type of parent is a generic type (new FXNodeActor<Pane> and FXNodeActor<Group>)
    - Have FXNodeActor ctor take a supplier so client code can pass in method that returns ???. Doesn't buy us anything because we still need a generic type to store result of supplier.
        - think this would be better handled by generics alone
- problem I can delay: Impl of each Actor class can determine what it is parented to. But, if I want to change and be able to parent my actors to a type other than Pane, I will need to change impl of all my Actor classes.
    - Decision: do simple implementation and wait for a compelling use case to switch a generic type
*/
public interface Actor extends Stepable, Reapable  {
}
