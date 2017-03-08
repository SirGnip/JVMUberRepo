package com.juxtaflux;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Can I reference "hbox1" inside the lambda?
// Can I use a context object to grab output of "new Control", add it to list of children
public class PaneBuilderExample2 extends AppFramework {
    public void start(Stage primaryStage) {
        VBox vbox = PaneBuilder2.make(new VBox(), (me) -> {
            me.getChildren().add(new Button("a"));
            me.getChildren().add(new Button("b"));
            me.getChildren().add(PaneBuilder2.make(new HBox(), (me2) -> {
                me2.getChildren().add(new Button("1"));
                me2.getChildren().add(new Button("2"));
                Button b3 = new Button("3");
                me2.getChildren().add(b3);
                b3.setOnAction((e) -> {
                    System.out.println("3 is clicked");
                });
            }));
            me.getChildren().add(PaneBuilder2.make(new HBox(), (me2) -> {
                me2.getChildren().add(new Button("x"));
                me2.getChildren().add(new Button("y"));
                me2.getChildren().add(new Button("z"));
            }));
            me.getChildren().add(new Button("****"));
        });
        commonSetup(primaryStage, vbox, "MinimalUIBuilder", 300, 200);
        AppFramework.dump(vbox);
    }
}
