# MyMachine
MyMachine is a simple visualisation tool to learn finite state machines.

## Running the application

Make sure that you have installed the Java Development Kit in version 11 or above.
If you have, then invoke

    ./gradlew run    # in Linux / OSX
    
or

    gradlew.bat run  # in Windows
    
in the main directory of the project to run the application

## Depoly the application

Run gradle with the target `shadowJar` to produce a standalone
executable JAR file (which can then be found in the directory
`./build/libs`.)

## Options

If your screen resolution does not allow the machine panel to fit on
the screen, it can be scaled down. Invoke the program with

    java -Dmymachine.panel.scale=<scale-factor> <jar-file>
    
to set the zoom level. Use a value less than 1 (e.g. `.85`) to 
show smaller machine elements.
