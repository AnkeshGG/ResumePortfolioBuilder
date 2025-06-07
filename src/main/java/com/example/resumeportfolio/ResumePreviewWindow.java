package com.example.resumeportfolio;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.List;
import java.util.Objects;

/**
 * Preview window for displaying formatted resume before PDF generation
 * Provides ATS-optimized formatting and professional layout
 */
public class ResumePreviewWindow {
    
    public static void show(Resume resume, boolean isDarkTheme) {
        Stage previewStage = new Stage();
        previewStage.initModality(Modality.APPLICATION_MODAL);
        previewStage.setTitle("Resume Preview - " + (resume.getName() != null ? resume.getName() : "Professional Resume"));
        
        // Create the preview content
        ScrollPane scrollPane = createPreviewContent(resume);
        
        // Create toolbar
        HBox toolbar = createToolbar(previewStage, resume);
        
        // Main layout
        BorderPane root = new BorderPane();
        root.setTop(toolbar);
        root.setCenter(scrollPane);
        
        // Create scene
        Scene scene = new Scene(root, 800, 900);
        
        // Apply theme
        if (isDarkTheme) {
            scene.getStylesheets().add(Objects.requireNonNull(ResumePreviewWindow.class.getResource("/styles/dark-theme.css")).toExternalForm());
            root.getStyleClass().add("dark-theme");
        } else {
            scene.getStylesheets().add(Objects.requireNonNull(ResumePreviewWindow.class.getResource("/styles/light-theme.css")).toExternalForm());
            root.getStyleClass().add("light-theme");
        }
        
        previewStage.setScene(scene);
        previewStage.setMinWidth(1000);
        previewStage.setMinHeight(800);
        previewStage.setMaximized(true);
        previewStage.centerOnScreen();
        previewStage.show();
    }
    
    private static HBox createToolbar(Stage stage, Resume resume) {
        HBox toolbar = new HBox(15);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(10, 15, 10, 15));
        toolbar.getStyleClass().add("toolbar");
        
        Label titleLabel = new Label("Resume Preview");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        titleLabel.getStyleClass().add("toolbar-title");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button generatePdfBtn = new Button("📥 Generate PDF");
        generatePdfBtn.getStyleClass().addAll("button", "success-button");
        generatePdfBtn.setOnAction(e -> {
            try {
                ResumeGenerator.generatePDF(resume);
                showAlert(stage, "Success", "Resume PDF generated successfully!", Alert.AlertType.INFORMATION);
            } catch (Exception ex) {
                showAlert(stage, "Error", "Failed to generate PDF: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });
        
        Button atsOptimizeBtn = new Button("🎯 ATS Tips");
        atsOptimizeBtn.getStyleClass().addAll("button", "info-button");
        atsOptimizeBtn.setOnAction(e -> showATSTips(stage));
        
        Button closeBtn = new Button("✖ Close");
        closeBtn.getStyleClass().addAll("button", "secondary-button");
        closeBtn.setOnAction(e -> stage.close());
        
        toolbar.getChildren().addAll(titleLabel, spacer, atsOptimizeBtn, generatePdfBtn, closeBtn);
        return toolbar;
    }
    
    private static ScrollPane createPreviewContent(Resume resume) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getStyleClass().add("resume-content");
        content.setMaxWidth(700);

        // Header Section
        addHeaderSection(content, resume);
        
        // Professional Summary
        if (resume.getProfessionalSummary() != null && !resume.getProfessionalSummary().trim().isEmpty()) {
            addSection(content, "PROFESSIONAL SUMMARY", resume.getProfessionalSummary());
        }
        
        // Core Skills
        if (resume.getSkills() != null && !resume.getSkills().trim().isEmpty()) {
            addSkillsSection(content, resume.getSkills());
        }
        
        // Professional Experience
        if (resume.getWorkExperiences() != null && !resume.getWorkExperiences().isEmpty()) {
            addExperienceSection(content, resume.getWorkExperiences());
        }
        
        // Education
        if (resume.getEducationList() != null && !resume.getEducationList().isEmpty()) {
            addEducationSection(content, resume.getEducationList());
        }
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        scrollPane.setPadding(new Insets(20));
        
        return scrollPane;
    }
    
    private static void addHeaderSection(VBox content, Resume resume) {
        VBox header = new VBox(8);
        header.setAlignment(Pos.CENTER);
        header.getStyleClass().add("resume-header");
        
        // Name
        Label nameLabel = new Label(resume.getName() != null ? resume.getName() : "");
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        nameLabel.getStyleClass().add("name-label");
        
        // Contact Information
        StringBuilder contactInfo = new StringBuilder();
        if (resume.getLocation() != null && !resume.getLocation().trim().isEmpty()) {
            contactInfo.append(resume.getLocation());
        }
        
        if (resume.getEmail() != null && !resume.getEmail().trim().isEmpty()) {
            if (!contactInfo.isEmpty()) contactInfo.append(" • ");
            contactInfo.append(resume.getEmail());
        }
        
        if (resume.getPhoneNumber() != null && !resume.getPhoneNumber().trim().isEmpty()) {
            if (!contactInfo.isEmpty()) contactInfo.append(" • ");
            contactInfo.append(resume.getPhoneNumber());
        }
        
        if (resume.getLinkedIn() != null && !resume.getLinkedIn().trim().isEmpty()) {
            if (!contactInfo.isEmpty()) contactInfo.append(" • ");
            contactInfo.append(resume.getLinkedIn());
        }
        
        if (!contactInfo.isEmpty()) {
            Label contactLabel = new Label(contactInfo.toString());
            contactLabel.setFont(Font.font("System", 12));
            contactLabel.getStyleClass().add("contact-label");
            header.getChildren().add(contactLabel);
        }
        
        header.getChildren().addFirst(nameLabel);
        content.getChildren().add(header);
        
        // Add separator line
        Separator separator = new Separator();
        separator.getStyleClass().add("header-separator");
        content.getChildren().add(separator);
    }
    
    private static void addSection(VBox content, String title, String text) {
        VBox section = new VBox(8);
        section.getStyleClass().add("resume-section");
        
        Label sectionTitle = new Label(title);
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        sectionTitle.getStyleClass().add("section-title");
        
        Label sectionContent = new Label(text);
        sectionContent.setFont(Font.font("System", 12));
        sectionContent.getStyleClass().add("section-content");
        sectionContent.setWrapText(true);
        
        section.getChildren().addAll(sectionTitle, sectionContent);
        content.getChildren().add(section);
    }
    
    private static void addSkillsSection(VBox content, String skills) {
        VBox section = new VBox(8);
        section.getStyleClass().add("resume-section");
        
        Label sectionTitle = new Label("CORE SKILLS");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        sectionTitle.getStyleClass().add("section-title");
        
        // Format skills nicely
        String[] skillArray = skills.split("[,;]");
        StringBuilder formattedSkills = new StringBuilder();

        for (String s : skillArray) {
            String skill = s.trim();
            if (!skill.isEmpty()) {
                if (!formattedSkills.isEmpty()) {
                    formattedSkills.append(" • ");
                }
                formattedSkills.append(skill);
            }
        }
        
        Label skillsContent = new Label(formattedSkills.toString());
        skillsContent.setFont(Font.font("System", 12));
        skillsContent.getStyleClass().add("section-content");
        skillsContent.setWrapText(true);
        
        section.getChildren().addAll(sectionTitle, skillsContent);
        content.getChildren().add(section);
    }
    
    private static void addExperienceSection(VBox content, List<Resume.WorkExperience> experiences) {
        VBox section = new VBox(15);
        section.getStyleClass().add("resume-section");
        
        Label sectionTitle = new Label("PROFESSIONAL EXPERIENCE");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        sectionTitle.getStyleClass().add("section-title");
        section.getChildren().add(sectionTitle);
        
        for (Resume.WorkExperience exp : experiences) {
            if (exp.getJobTitle() == null || exp.getJobTitle().trim().isEmpty()) {
                continue;
            }
            
            VBox expBox = new VBox(4);
            expBox.getStyleClass().add("experience-item");
            
            // Job Title
            Label jobTitle = new Label(exp.getJobTitle());
            jobTitle.setFont(Font.font("System", FontWeight.BOLD, 13));
            jobTitle.getStyleClass().add("job-title");
            expBox.getChildren().add(jobTitle);
            
            // Company and Location
            StringBuilder companyInfo = new StringBuilder();
            if (exp.getCompany() != null && !exp.getCompany().trim().isEmpty()) {
                companyInfo.append(exp.getCompany());
            }
            
            if (exp.getLocation() != null && !exp.getLocation().trim().isEmpty()) {
                if (!companyInfo.isEmpty()) companyInfo.append(", ");
                companyInfo.append(exp.getLocation());
            }
            
            if (!companyInfo.isEmpty()) {
                Label company = new Label(companyInfo.toString());
                company.setFont(Font.font("System", FontWeight.BOLD, 12));
                company.getStyleClass().add("company-label");
                expBox.getChildren().add(company);
            }
            
            // Duration
            if (exp.getDuration() != null && !exp.getDuration().trim().isEmpty()) {
                Label duration = new Label(exp.getDuration());
                duration.setFont(Font.font("System", 11));
                duration.getStyleClass().add("duration-label");
                expBox.getChildren().add(duration);
            }
            
            // Achievements
            if (exp.getAchievements() != null && !exp.getAchievements().isEmpty()) {
                VBox achievementsBox = new VBox(2);
                achievementsBox.setPadding(new Insets(5, 0, 0, 15));
                
                for (String achievement : exp.getAchievements()) {
                    if (achievement != null && !achievement.trim().isEmpty()) {
                        Label bullet = new Label("• " + achievement.trim());
                        bullet.setFont(Font.font("System", 11));
                        bullet.getStyleClass().add("achievement-item");
                        bullet.setWrapText(true);
                        achievementsBox.getChildren().add(bullet);
                    }
                }
                expBox.getChildren().add(achievementsBox);
            }
            
            section.getChildren().add(expBox);
        }
        
        content.getChildren().add(section);
    }
    
    private static void addEducationSection(VBox content, List<Resume.Education> educationList) {
        VBox section = new VBox(12);
        section.getStyleClass().add("resume-section");
        
        Label sectionTitle = new Label("EDUCATION");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        sectionTitle.getStyleClass().add("section-title");
        section.getChildren().add(sectionTitle);
        
        for (Resume.Education edu : educationList) {
            if (edu.getDegree() == null || edu.getDegree().trim().isEmpty()) {
                continue;
            }
            
            VBox eduBox = new VBox(3);
            eduBox.getStyleClass().add("education-item");
            
            // Degree
            Label degree = new Label(edu.getDegree());
            degree.setFont(Font.font("System", FontWeight.BOLD, 12));
            degree.getStyleClass().add("degree-label");
            eduBox.getChildren().add(degree);
            
            // Institution
            if (edu.getInstitution() != null && !edu.getInstitution().trim().isEmpty()) {
                Label institution = new Label(edu.getInstitution());
                institution.setFont(Font.font("System", 12));
                institution.getStyleClass().add("institution-label");
                eduBox.getChildren().add(institution);
            }
            
            // Year and GPA
            StringBuilder details = new StringBuilder();
            if (edu.getYear() != null && !edu.getYear().trim().isEmpty()) {
                details.append(edu.getYear());
            }
            
            if (edu.getGpa() != null && !edu.getGpa().trim().isEmpty()) {
                if (!details.isEmpty()) details.append(" • ");
                details.append("GPA: ").append(edu.getGpa());
            }
            
            if (!details.isEmpty()) {
                Label detailsLabel = new Label(details.toString());
                detailsLabel.setFont(Font.font("System", 11));
                detailsLabel.getStyleClass().add("education-details");
                eduBox.getChildren().add(detailsLabel);
            }
            
            section.getChildren().add(eduBox);
        }
        
        content.getChildren().add(section);
    }
    
    private static void showAlert(Stage parent, String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.initOwner(parent);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private static void showATSTips(Stage parent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(parent);
        alert.setTitle("ATS Optimization Tips");
        alert.setHeaderText("Make Your Resume ATS-Friendly");
        
        String tips = """
                🎯 ATS OPTIMIZATION CHECKLIST:
                
                ✅ Use standard section headers (Experience, Education, Skills)
                ✅ Include relevant keywords from job descriptions
                ✅ Use simple, clean formatting without tables or columns
                ✅ Save and submit as PDF to preserve formatting
                ✅ Use standard fonts (Arial, Helvetica, Times New Roman)
                ✅ Include your contact information in plain text
                ✅ Use bullet points for achievements and responsibilities
                ✅ Quantify achievements with numbers and percentages
                ✅ Spell out abbreviations (also include abbreviated form)
                ✅ Use action verbs to start bullet points
                
                🚫 AVOID:
                • Images, graphics, or fancy designs
                • Headers and footers for important information
                • Tables and columns for layout
                • Unusual fonts or excessive formatting
                • Acronyms without spelling them out
                """;
        
        alert.setContentText(tips);
        alert.getDialogPane().setPrefWidth(500);
        alert.getDialogPane().setPrefHeight(400);
        alert.showAndWait();
    }
}