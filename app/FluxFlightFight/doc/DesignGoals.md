# Design goals:

- support scene graph animation and motion from simulation
- Actors are game objects that live in the scene graph, often manage their own lifetime and simulation
    - an Actor is usually "stepable", "reapable", "drawable"
- the way interactive game objects are controlled should support both user input (gameplay) AND programmatic control (testing)
- animation should be time-delta based and not tick based
- let people register their controller, and don't have hardcoded user input configs
    - User input can be any mix of mouse, keyboard, or joystick (may want to limit this to make it easier)
        - at game startup, have people pick their controller sequentially, and they are told what buttons do what.
            - first controller to be touched causes game to say "This is controller one." An entry shows up in a list. That player can then choose config (color, nickname, etc). the next person to press the controller becomes player 2. Do, I have game orchestrate who presses what when to eliminate confusion?
- Be able to stub out (dependency inject, mocks, etc.) subsystems (audio, user input, graphics, time, simulation) so that it is easy to test subsystems in isolation. When designing app, try to keep as much core logic decoupled from exterior systems
	- for unit testing it shouldn't require outside "services". For a game, think of "graphics", "sound", "input", "file system" as "external services". So, my unit tests can run without "external services". Then, have another layer of testing where I do "integration testing" where i *do* depend on external services (graphics, sound, input, file system) but don't test as deeply. If I am in experimental mode, don't do any testing.
    - dependency injection?
        - Ex: inject a do-nothing dummy SoundManager into app for testing
- use text files or Excel as a level editing tool so that boys can be involved in making levels.
- try to do keep libraries and apps in one multi-project so that IntelliJ refactorings can affect all of it at once
    - as I'm learning Java and exploring the game, this will allow me to change things much more quickly
    - as project ages, I might want to break out the library into its own external jar and individual apps into their own repos for ease of sharing with others. 
    
# Tech design goals

- use 3rd party libraries pretty much wherever possible
- javadoc
    - keep it bare-bones
- unit testing
- automation-assisted manual testing (supported by having code that runs a user through multiple manual steps so user doesn't have to think about it...)
- use packages that expose minimal interfaces
    - how do i communicate what classes are meant for use outside of the package and which are internal to package? (think about Java 9 "modules" for inspiration and education)
- create shared libraries that I can extract later, but don't go through the work of extracting now.
- handling null properly. Be very strict.
- design for testability (unit and integration)