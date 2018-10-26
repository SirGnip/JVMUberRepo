How to take git clone and build/install it

# Setup project
- create IntelliJ project
- add library
    - add modules to project via Project Structure->Project Settings->Modules->Dependencies (Or, you might have to add this to Libraries? Can't remember.)
        - JavaFXUtils.jar
        - commons-lang3-3.5.jar
        - JUnit4 (some more automated way to add this, but can't remember how)
- create .jar file
    - Project Structure->Artifacts
    - create new jar
    - expand your project in the "Available Elements" list at the right
    - right click on the library and select "Extract into output root"
        - JavaFXUtils.jar
        - commons-lang3-3.5.jar
    - Create "entry point" for .jar by creating a Manifest file and setting main class


# Building
- Build->Build Project
- Build->Build Artifacts...->Build
- cd <proj dir>/scripts/
- ./makeInstallDir.sh (follow directions to install)
