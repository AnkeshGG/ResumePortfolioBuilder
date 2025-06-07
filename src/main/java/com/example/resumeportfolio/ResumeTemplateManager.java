package com.example.resumeportfolio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Manages saving and loading of resume templates as JSON files.
 * Provides functionality to serialize Resume objects to JSON format
 * and deserialize them back to Resume objects.
 */
public class ResumeTemplateManager {
    
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    
    /**
     * Saves a Resume object to a JSON file.
     * 
     * @param resume The Resume object to save
     * @param file The file to save the template to
     * @throws IOException if there's an error writing to the file
     * @throws IllegalArgumentException if resume or file is null
     */
    public static void saveTemplate(Resume resume, File file) throws IOException {
        if (resume == null) {
            throw new IllegalArgumentException("Resume cannot be null");
        }
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        
        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            gson.toJson(resume, writer);
        } catch (IOException e) {
            throw new IOException("Failed to save template to file: " + file.getAbsolutePath(), e);
        }
    }
    
    /**
     * Loads a Resume object from a JSON file.
     * 
     * @param file The file to load the template from
     * @return The Resume object loaded from the file
     * @throws IOException if there's an error reading the file
     * @throws IllegalArgumentException if file is null or doesn't exist
     * @throws JsonSyntaxException if the JSON format is invalid
     */
    public static Resume loadTemplate(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist: " + file.getAbsolutePath());
        }
        if (!file.canRead()) {
            throw new IOException("Cannot read file: " + file.getAbsolutePath());
        }
        
        try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {
            Resume resume = gson.fromJson(reader, Resume.class);
            if (resume == null) {
                throw new JsonSyntaxException("Invalid JSON format or empty file");
            }
            return resume;
        } catch (IOException e) {
            throw new IOException("Failed to load template from file: " + file.getAbsolutePath(), e);
        } catch (JsonSyntaxException e) {
            throw new JsonSyntaxException("Invalid JSON format in file: " + file.getAbsolutePath(), e);
        }
    }
    
    /**
     * Validates if a file can be used as a template file.
     * 
     * @param file The file to validate
     * @return true if the file is valid for template operations
     */
    public static boolean isValidTemplateFile(File file) {
        if (file == null) {
            return false;
        }
        
        // Check if it's a JSON file
        String fileName = file.getName().toLowerCase();
        if (!fileName.endsWith(".json")) {
            return false;
        }
        
        // If file exists, check if it's readable
        if (file.exists()) {
            return file.canRead() && file.isFile();
        }
        
        // If file doesn't exist, check if parent directory is writable
        File parentDir = file.getParentFile();
        return parentDir != null && parentDir.exists() && parentDir.canWrite();
    }
    
    /**
     * Creates a backup of an existing template file before overwriting.
     * 
     * @param originalFile The original file to backup
     * @return The backup file created, or null if backup failed
     */
    public static File createBackup(File originalFile) {
        if (originalFile == null || !originalFile.exists()) {
            return null;
        }
        
        try {
            String backupFileName = originalFile.getName().replaceFirst("(\\.[^.]*)?$", "_backup$1");
            File backupFile = new File(originalFile.getParent(), backupFileName);
            
            // If backup already exists, add timestamp
            if (backupFile.exists()) {
                long timestamp = System.currentTimeMillis();
                backupFileName = originalFile.getName().replaceFirst("(\\.[^.]*)?$", "_backup_" + timestamp + "$1");
                backupFile = new File(originalFile.getParent(), backupFileName);
            }
            
            // Copy the original file to backup
            java.nio.file.Files.copy(originalFile.toPath(), backupFile.toPath());
            return backupFile;
            
        } catch (IOException e) {
            System.err.println("Failed to create backup: " + e.getMessage());
            return null;
        }
    }
}