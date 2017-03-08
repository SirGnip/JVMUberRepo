package com.juxtaflux;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;

// Can I reference "hbox1" inside the lambda?
// Can I use a context object to grab output of "new Control", add it to list of children
public class PaneBuilderExample extends AppFramework {
    public void start(Stage primaryStage) {
        PaneBuilder<VBox> wrap = new PaneBuilder<>(new VBox(), () -> {
            Button b1 = new Button("a");
            Button b2 = new Button("b");
            PaneBuilder<HBox> hbox1 = new PaneBuilder<>(new HBox(), () -> {
                Button a = new Button("3");
                Button b = new Button("4");
                return Arrays.asList(a, b);
            });
            PaneBuilder<HBox> hbox2 = new PaneBuilder<>(new HBox(), () -> {
                Button a = new Button("8");
                a.setOnAction((e) -> {
                    System.out.println("clicked 8");
                });
                Button b = new Button("9");
                b.setOnAction((e) -> {
                    System.out.println("clicked 9");
                });
                Button c = new Button("0");
                c.setOnAction((e) -> {
                    System.out.println("clicked 0");
                });
                return Arrays.asList(a, b, c);
            });
            Button b3 = new Button("c");
            return Arrays.asList(b1, b2, hbox1.pane, hbox2.pane, b3);
        });

        commonSetup(primaryStage, wrap.pane, "MinimalUIBuilder", 300, 200);
        AppFramework.dump(wrap.pane);
    }
}
