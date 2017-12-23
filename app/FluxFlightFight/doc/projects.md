

# Milestone 1: frenzy game
- when I'm done with this milestone, I will have something playable and fun
- prioritize breadth of implementation first because it is fun, get something playable, and exposes more unknowns) (also prioritize "game" features over "supporting stuff" so that I can get something "fun" asap
- goal: the minimal amount of work to get a playable game with multiple humans, multiple NPC's. Round takes 3 minutes. One point for each NPC and two points for each player unseated. Show score.
    - optimize order of working on stuff for exposing maximum number of "useable" features to humans (so they can play with stuff earlier) 
    - keyboard and mouse only?
    - hardcoded input configs
    - basic game flow (title screen, choose # of players, round start, round end and show score, play again)

Sequence:
- put in source control (uber repo), keep it local for now
    - x turn on source control integration in IntelliJ
- x the "higher joust" mechanic
- x death and respawn of player
- x rework input behavior: should be two directions and one to flap, not 3 flapping directions
    - idea of how to track state: https://stackoverflow.com/a/13017419
        - though, i could do a big array like this, or i could just track the state im interested in for each object
    - x have visual to reflect direction of bird?
    - x can i have that may keys pressed at the same time (4 players, each player will be holding a direction key down)
    - one part of this will be determining state of keys (is it down?) which means I'll have to deal with key-repeat.
        - it appears that a keyboard is split into zones. a max number of keys *in a certain zone* can be held down at once. I'll probably need to choose keys that span zones.
        - in a text editor, press three keys next to each other at the same time. if they are in the same zone, you will hear a "beep" and see no characters typed. Keep hitting these three keys
        - it feels like each zone can handle a max of two keypresses at the same time. 
        - x maybe write a utility to print the state of each key (detects key)
            - x make sure key repeat is not an issue
        - Keyboard "rollover" https://en.wikipedia.org/wiki/Rollover_(key)    
        
- scoreboard
- add level obstacles (platforms to land on)
- (at this point, decide if it would be better to work on "walking" first, or "NPC's" first)
- NPC
    - have an eye to Actor (visual w/ simulation) being separate from Player (Actor that someone controls has player specific data like score, etc.) being separate from Controller (what controls an Actor... keyboard presses? automation?)
        - goal: have Actors that can be controlled by automation (NPC) or humans (uses InputConfigs). Some NPC behavior may be shared with Player (scores, respawning), while some is unique to NPC. 
        - goal: how do i share certain behavior while keeping it separate?
        - goal: have a lot of functionality that can be driven by either user input and programmatic control.
    - do i have separate classes to track things differently for "player" and "enemy" (like score, etc). Or, are NPC's just autoated players that are completely like players (have score, can be controlled by input, etc))
- death and respawn of NPC
- very simple NPC behavior (hover, random movement with hovering, etc.)
- walking on ground physics
- transitions between flying and running
- physics of player responding to obstacles (wrap around screen? bounce off of wall? bounce off platform? walk vs fly?)
- "shell" (title screen, title screen, initial question, gameplay, round end)
- bonus: things that are just a bit more "polish" that might be nice:
    - keyboard w/ no repeat
    - fullscreen and try on TV
    - a pass on sound (flapping w/ variety, crash, walking)
    - NPC's with basic patterns (hover, oscillate back and forth, a small set of behaviors they randomly pick from)
        - hover, move left, move right, rise, fall, fall and walk, fly towards nearest character, fly away from character 
    - tweak falling/flying physics (might need to tweak scale of players and size of screen)
    - git in git, maybe github after this is done as things will have stabalized some
    - slight rework on bird visuals
- Now that this is done, do i have all of my external systems (input, sound, graphics, logging? packaging?, fullscreen?, ui, input registration, controllers)
    - at this point my external systems aren't perfect, flexible, or reusable. but I've touched everything. Will inform future direction.
    - collision detection
    - actors w/ lifetime
    - (automated controllers, input based ocntorllers, audio manager, actors, unit testing, dependency injection, game shell state-machine, animation, simulation, particle system, actors, audio authoring pipeline, graphics authoring pipeline, level design pipeline, concurrency?, multi-jar, error handling (nulls, checked/nonchecked exceptions?, startup), build and deploy dependencies (have batch script to deploy everything to a folder I can copy to another machine to run w/ start script?))

# Future Milestone brainstorm
  - support a "round" system like Snake that allows for different game goals, objectives, scoring
  - level editing tools
  - a pass on graphics
  - fancy FX for player death and other events
  - ambient sounds (bubbling, occasional wind, dripping)  
  - input configuration
  

# action backlog
- have a simple app that takes an actor, builds it on the scene graph, and then plays top-level animation on it (translateions, rotations, scales, combos) to make sure everything is parented correctly. Then, it parents it under some parent node that is then animated to make sure all looks ok.
    - use canned animation I can simply re-apply
- guabva vs apache commons
- go from vector to angle
- design controller system
    - controller controls player
    - controller can be tied to input controller or generated automated control (for testing/demo)
    - can i use any design patterns?
    - what needs to change? what will stay the same? what do I need to replace?
- get in git after you have some code and something slightly runnable and a solution for "experimental" code
    - do i put experimental stuff external to this project?
- joystick
    - hook into JavaFX event system (https://stackoverflow.com/questions/27416758/how-to-emit-and-handle-custom-events)
- unittesting (get junit and mockito set up?)
- authoring
    - graphics
        - see if i can author in inkscape, export to svg, import to JavaFX
    - audio
        - all my audio tools, bring into JavaFX as .wav
            - can JavaFX do .mp3?
    - level
        - if grid based: Excel? Text file? json?
        - if arbitrary pixel based: Inkscape
- release
    - build to one jar
    - package all assets (graphics, level, sound) into Jar

