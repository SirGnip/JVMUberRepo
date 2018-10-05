package com.juxtaflux.app;

/** Unused Game class.
Design brainstorm
 - Games have "game objects" (players, enemies, projectiles, pickups, obstacles, map, etc)
 - game objects do "internal" and "external" stuff
     - internal (object has its own activities)
         - managing simulation
         - secondary animation
         - internal state
     - external (objects collaborate)
         - object collisions
         - objects can query each other
         - objects can send messages to each other
 - there will probably be higher level objects that manage interactions between game objects
 - game objects must manage the following:
     - creation (internal state and its visual representation in scene graph)
     - simulation (advance the simulation, generate events, respond to messages)
     - end of lifetime (when do I go away? remove myself from scene graph and remove myself or notify all other listening objects)
    - Implementation note: these could probably be represented by individual interfaces
 - can game objects be composable/nested?

 create and make visual representation in scene graph
 - Q: what is role of scene graph? visual only? Input? messaging?


 a brainstorm for how integration between game object and scene graph could be implemented

 - Game obj is parent: game obj has reference to scene graph object, uses its methods to query/interact with scene graph
 - Game obj is parent: game obj has references to specific properties on scene graph object Node that it needs access to
 - what happens if node is removed from scene graph. what does game object do? how does it handle this?
 - Scene Graph is parent: scene graph puts "game object" data/object in Node's property map
 - create class derived from Scene graph and add data to it to support simulation.
 - use Binding so that every time the velocity vector changes, the x & y automatically change
 - do something with observables so that when something changes (simulation), other stuff changes


 Implementation ideas
 - instead of making concrete Actor classes for different shapes, have a generic class that accepts a callback that builds an arbitrary scene graph object and returns it.  Maybe pass in some kind of "creation" object? Strategy pattern? lambdas? This parameterizes the visuals.
 - I could potentially parameterize lifetime semantics as well (lifetime)? Though, it would probably just boil down to "die after elapsed time" or "stay alive until told to die"
 - a game object (or Actor?) could be "SceneGraphAble" (names: SceneGraphBuilder? NodeCreator? NodesCreator? NodesBuilder?), "Stepable", "Reapable"
*/
public class GameApp {
}
