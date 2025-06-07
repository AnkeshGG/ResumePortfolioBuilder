package com.example.resumeportfolio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file from the resources
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            ThemeManager.getInstance().applyTheme(scene);

            // Configure the stage
            primaryStage.setTitle("Professional Resume Builder - ATS Optimized");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(700);

            // Set application icon (if present in the resources)
            try {
                Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/app-icon.png")));
                primaryStage.getIcons().add(icon);
            } catch (Exception e) {
                System.out.println("App icon not found, continuing without icon");
            }

            primaryStage.setMaximized(true);
            primaryStage.show();

            System.out.println("Resume Builder Application started successfully!");

        } catch (Exception e) {
            System.err.println("Failed to start application: " + e.getMessage());
            e.printStackTrace();
            // Optionally, display an alert dialog to notify the user
        }
    }

    @Override
    public void stop() {
        System.out.println("Resume Builder Application is closing...");
        // Perform any necessary cleanup operations here.
    }

    public static void main(String[] args) {
        // Set system properties for JavaFX performance; the preloader property has been removed.
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.text", "t2k");
        System.setProperty("glass.win.uiScale", "1.0");
        launch(args);
    }
}
