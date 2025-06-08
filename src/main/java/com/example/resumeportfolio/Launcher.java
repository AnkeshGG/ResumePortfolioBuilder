package com.example.resumeportfolio;

/**
 * Launcher class for JavaFX Application
 * This class is required to properly launch JavaFX applications from a fat JAR
 * without module-path issues.
 */
public class Launcher {
    public static void main(String[] args) {
        // Set DPI scaling properties explicitly before launching the app
        System.setProperty("prism.text", "t2k");
        System.setProperty("glass.gtk.uiScale", "2"); // or adjust as needed
        System.setProperty("glass.win.uiScale", "1.5"); // if running on Windows
        System.setProperty("prism.allowhidpi", "true");

        // Launch the JavaFX application
        App.main(args);
    }
}
