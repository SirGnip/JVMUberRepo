package com.juxtaflux;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Main extends Application {
    public static Controller staticController;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(Main::handleException);
        final String UI_LAYOUT_FILENAME = "/ClipDashboard.fxml";
        final String ICON_FILENAME = "/ClipDashboardIcon.ico";
        System.out.println("Application starting. Loading UI layout from " + UI_LAYOUT_FILENAME);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(UI_LAYOUT_FILENAME));
        Parent root;
        try {
            root = loader.load();
        } catch (Exception e) {
            System.out.println("ERROR: Problem loading UI layout: " + UI_LAYOUT_FILENAME);
            throw e;
        }

        System.out.println("Loading app icon: " + ICON_FILENAME);
        InputStream iconInputStream = getClass().getResourceAsStream(ICON_FILENAME);
        if (iconInputStream == null) {
            throw new Exception("Problem loading app icon: " + ICON_FILENAME);
        }
        primaryStage.getIcons().add(new Image(iconInputStream));
        primaryStage.setTitle(Config.APP_TITLE);
        primaryStage.setScene(new Scene(root, Config.APP_WIDTH, Config.APP_HEIGHT));
        primaryStage.show();
        Controller controller = loader.getController();
        controller.onReady(primaryStage);
        AppFramework.dump(root);
        staticController = controller;
    }

    private static void handleException(Thread t, Throwable e) {
        e.printStackTrace(System.out);
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        staticController.statusBar.showErr("EXCEPTION! " + e.getClass().getName() + " See 'Log' tab for details");
        staticController.log.insertText(0, "Got exception: " + e.toString() + "\n" + sw.toString());
    }

    public static void main(String[] args) {
        launch(args);
    }
}


