package com.juxtaflux;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class AppFramework extends Application {
    /** Simplify some of the boilerplate needed to create UI's in JavaFX. */
    public void commonSetup(Stage primaryStage, Parent root, String stageTitle, int stageWidth, int stageHeight) {
        Scene scene = new Scene(root, stageWidth, stageHeight);
        primaryStage.setTitle(stageTitle);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /** Utility to dump JavaFX scene graph Reference: http://stackoverflow.com/a/21176630 */
    public static void dump(Node n) {
        dump(n, 999, 0);
    }

    /** traverse the JavaFX scene graph and print out the basic hierarchy */
    private static void dump(Node n, int maxDepth, int curDepth) {
        if (n.getClass().getName().startsWith("com.sun")) {
            return;
        }

        final String oneIndent = "  ";
        String indent = "";

        for (int i = 0; i < curDepth; ++i) {
            indent += oneIndent;
        }
        String msg = indent + n.getClass().getName() + ((n.getId() != null) ? " id:" + n.getId() : "");
        msg += getNodeSpecificTag(n);
        System.out.println(msg);
        if (curDepth < maxDepth && n instanceof Parent && dumpDoRecurse(n)) {
            for (Node child : ((Parent) n).getChildrenUnmodifiable()) {
                dump(child, maxDepth, curDepth + 1);
            }
        }
    }
    private static boolean dumpDoRecurse(Node n) {
        return ! (n instanceof Labeled);
    }
    private static String getNodeSpecificTag(Node n) {
        String s = " ";
        if (n instanceof Button) {
            s += "'" + ((Button) n).getText() + "'";
        }
        if (n instanceof Pane) {
            s += "childCnt=" + ((Pane) n).getChildrenUnmodifiable().size();
        }
        if (n instanceof ListView) {
            s += "itemCnt=" + ((ListView) n).getItems().size();
        }
        if (n instanceof TextArea) {
            s += "len=" + ((TextArea) n).getText().length();
        }
        return s;
    }
}
