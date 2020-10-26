# Java uber repo

This is an uber repo that contains multiple applications and libraries.  The applications are mostly game experiments and written in Java and Groovy, relying on JavaFX for graphics and audio. 

# Individual components

Apps
- [app/FluxFlightFight](app/FluxFlightFight): Work-in-progress: Multiplayer game experiment. Fly and hit your opponent higher than they hit you.  Gamepad and keyboard support for 2-8+ players. 
- [app/ClipDashboard](app/ClipDashboard): Desktop utilitiy for managing and mutating text in the system clipboard.  

Libraries
- [lib/JavaFXUtils](lib/JavaFXUtils): classes to assist with JavaFX.
- [lib/standInResources](lib/standInResources): library containing small and simple media resources (images, audio, text) for experimental purposes. 


# Release Notes

Can see brief release notes with:

    git tag -l --format="%(refname:short) %(taggerdate:short) %(objectname:short) %(contents:lines=1)"

![Hits](https://hitcounter.pythonanywhere.com/count/tag.svg?url=https%3A%2F%2Fgithub.com%2FSirGnip%2FJVMUberRepo)
