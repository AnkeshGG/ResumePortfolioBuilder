package com.example.resumeportfolio;

import javafx.scene.Scene;

import java.util.Objects;
import java.util.prefs.Preferences;

/**
 * Manages theme switching and persistence for the Resume Builder application
 */
public class ThemeManager {

    // Theme constants
    public static final String LIGHT_THEME = "/styles/light-theme.css";
    public static final String DARK_THEME = "/styles/dark-theme.css";

    // Preference keys
    private static final String THEME_PREFERENCE_KEY = "selected_theme";
    private static final String LIGHT_THEME_VALUE = "light";
    private static final String DARK_THEME_VALUE = "dark";

    // Singleton instance
    private static ThemeManager instance;
    private boolean isDarkTheme;
    private final Preferences preferences;

    private ThemeManager() {
        this.preferences = Preferences.userNodeForPackage(ThemeManager.class);
        loadThemePreference();
    }

    /**
     * Get the singleton instance of ThemeManager
     */
    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    /**
     * Apply the current theme to a scene
     */
    public void applyTheme(Scene scene) {
        scene.getStylesheets().clear();
        String themeResource = isDarkTheme ? DARK_THEME : LIGHT_THEME;

        try {
            String themeUrl = Objects.requireNonNull(getClass().getResource(themeResource)).toExternalForm();
            scene.getStylesheets().add(themeUrl);
        } catch (Exception e) {
            System.err.println("Failed to load theme: " + themeResource);
            e.printStackTrace();
            // Fallback to light theme
            if (isDarkTheme) {
                try {
                    String lightThemeUrl = Objects.requireNonNull(getClass().getResource(LIGHT_THEME)).toExternalForm();
                    scene.getStylesheets().add(lightThemeUrl);
                    isDarkTheme = false;
                } catch (Exception fallbackException) {
                    System.err.println("Failed to load fallback light theme");
                }
            }
        }
    }

    /**
     * Toggle between light and dark themes
     */
    public void toggleTheme(Scene scene) {
        isDarkTheme = !isDarkTheme;
        applyTheme(scene);
        saveThemePreference();
    }

    /**
     * Set specific theme
     */
    public void setTheme(Scene scene, boolean darkTheme) {
        if (this.isDarkTheme != darkTheme) {
            this.isDarkTheme = darkTheme;
            applyTheme(scene);
            saveThemePreference();
        }
    }

    /**
     * Get current theme state
     */
    public boolean isDarkTheme() {
        return isDarkTheme;
    }

    /**
     * Get theme display name
     */
    public String getCurrentThemeName() {
        return isDarkTheme ? "🌙 Dark Mode" : "☀ Light Mode";
    }

    /**
     * Get theme toggle button text
     */
    public String getToggleButtonText() {
        return isDarkTheme ? "☀ Light Mode" : "🌙 Dark Mode";
    }

    /**
     * Load theme preference from system preferences
     */
    private void loadThemePreference() {
        String savedTheme = preferences.get(THEME_PREFERENCE_KEY, LIGHT_THEME_VALUE);
        isDarkTheme = DARK_THEME_VALUE.equals(savedTheme);
    }

    /**
     * Save current theme preference
     */
    private void saveThemePreference() {
        String themeValue = isDarkTheme ? DARK_THEME_VALUE : LIGHT_THEME_VALUE;
        preferences.put(THEME_PREFERENCE_KEY, themeValue);

        try {
            preferences.flush();
        } catch (Exception e) {
            System.err.println("Failed to save theme preference: " + e.getMessage());
        }
    }

    /**
     * Reset theme to default (light)
     */
    public void resetToDefault(Scene scene) {
        isDarkTheme = false;
        applyTheme(scene);
        saveThemePreference();
    }

    /**
     * Apply theme to dialog panes for consistent styling
     */
    public void applyThemeToDialog(javafx.scene.control.DialogPane dialogPane) {
        dialogPane.getStylesheets().clear();
        String themeResource = isDarkTheme ? DARK_THEME : LIGHT_THEME;

        try {
            String themeUrl = Objects.requireNonNull(getClass().getResource(themeResource)).toExternalForm();
            dialogPane.getStylesheets().add(themeUrl);
        } catch (Exception e) {
            System.err.println("Failed to apply theme to dialog: " + e.getMessage());
        }
    }
}