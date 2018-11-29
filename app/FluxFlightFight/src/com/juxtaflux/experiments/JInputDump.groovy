package com.juxtaflux.experiments

import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.stage.Stage
import net.java.games.input.Component
import net.java.games.input.Controller
import net.java.games.input.ControllerEnvironment


class JInputDump extends ExampleBase {
    @Override
    void buildRoot(Stage stage, Pane pane) {
        Text info = new Text(20, 50,"Dumping input controller info to stdout")
        info.setFill(Color.GREEN)
        info.setFont(new Font(40))
        pane.getChildren().addAll(info)

        printControllers()
        println "\n\n"
        printComponents()
    }

    void printControllers() {
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers()
        printf "There are %d controllers\n", controllers.size()

        printf "           Prt   PortType       Type Rm Name\n"
        for (Controller cont : controllers) {
            printf "Controller %3d %-10s %-10s %2d '%s'\n",
                    cont.getPortNumber(),
                    cont.getPortType(),
                    cont.getType(),
                    cont.getRumblers().size(),
                    cont.getName()
        }
    }

    void printComponents() {
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers()
        printf "There are %d controllers\n", controllers.size()

        for (Controller cont : controllers) {
            printf("Controller: type:%s port#:%d portType:%s RumblerCount:%d name:'%s' componentCount:%d\n",
                    cont.getType(),
                    cont.getPortNumber(),
                    cont.getPortType(),
                    cont.getRumblers().size(),
                    cont.getName(),
                    cont.getComponents().size()
            )
            for (Component comp: cont.getComponents()) {
                printf("    Component: %-15s %-15s %-15s %5b %5b %.3f\n",
                        comp.getClass().getSimpleName(),
                        comp.getIdentifier(),
                        comp.getName(),
                        comp.isRelative(),
                        comp.isAnalog(),
                        comp.getDeadZone()
                )

            }
            println ''
        }
    }

}
