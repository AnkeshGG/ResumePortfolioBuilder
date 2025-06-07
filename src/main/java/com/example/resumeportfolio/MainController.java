package com.example.resumeportfolio;

import com.example.resumeportfolio.Resume.Education;
import com.example.resumeportfolio.Resume.WorkExperience;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    // Personal Information Fields
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField locationField;
    @FXML private TextField linkedInField;

    // Professional Summary
    @FXML private TextArea summaryArea;
    @FXML private Label summaryCountLabel;

    // Skills
    @FXML private TextArea skillsArea;

    // Dynamic Containers
    @FXML private VBox educationContainer;
    @FXML private VBox experienceContainer;

    // Buttons
    @FXML private Button themeToggleBtn;
    @FXML private Button addEducationBtn;
    @FXML private Button addExperienceBtn;
    @FXML private Button previewBtn;
    @FXML private Button generateBtn;
    @FXML private Button saveTemplateBtn;
    @FXML private Button loadTemplateBtn;

    // Status
    @FXML private Label statusLabel;

    private boolean isDarkTheme = false;
    private List<EducationItemController> educationControllers = new ArrayList<>();
    private List<ExperienceItemController> experienceControllers = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupCharacterCounters();
        setupInitialEducationAndExperience();
        updateStatus("Ready to build your professional resume");
    }

    private void setupCharacterCounters() {
        summaryArea.textProperty().addListener((obs, oldText, newText) -> {
            int length = newText != null ? newText.length() : 0;
            summaryCountLabel.setText(length + "/300 characters");
            if (length > 300) {
                summaryCountLabel.setStyle("-fx-text-fill: #f56565;");
            } else {
                summaryCountLabel.setStyle("-fx-text-fill: #a0aec0;");
            }
        });
    }

    private void setupInitialEducationAndExperience() {
        // Add one education item by default
        addEducation();
        // Add one experience item by default
        addExperience();
    }

    @FXML
    private void switchTheme() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        Scene scene = stage.getScene();

        // Use centralized ThemeManager
        ThemeManager themeManager = ThemeManager.getInstance();
        themeManager.toggleTheme(scene);

        // Update the toggle button label
        themeToggleBtn.setText(themeManager.getToggleButtonText());

        updateStatus("Theme switched to " + themeManager.getCurrentThemeName());
    }

    @FXML
    private void initialize() {
        themeToggleBtn.setText(ThemeManager.getInstance().getToggleButtonText());
    }

    @FXML
    private void addEducation() {
        try {
            VBox educationItem = createEducationItem();
            educationContainer.getChildren().add(educationItem);
            updateStatus("Education entry added");
        } catch (Exception e) {
            showAlert("Error", "Failed to add education entry: " + e.getMessage());
        }
    }


    private VBox createEducationItem() {
        VBox container = new VBox(10);
        container.getStyleClass().add("education-item");

        // Header with remove button
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label headerLabel = new Label("Education #" + (educationControllers.size() + 1));
        headerLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button removeBtn = new Button("✖ Remove");
        removeBtn.getStyleClass().add("remove-button");
        removeBtn.setOnAction(e -> {
            educationContainer.getChildren().remove(container);
            updateEducationNumbers();
            updateStatus("Education entry removed");
        });

        header.getChildren().addAll(headerLabel, spacer, removeBtn);

        // Form fields
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);

        TextField degreeField = new TextField();
        degreeField.setPromptText("e.g., Bachelor of Science in Computer Science");
        degreeField.getStyleClass().add("modern-textfield");

        TextField institutionField = new TextField();
        institutionField.setPromptText("e.g., University of Technology");
        institutionField.getStyleClass().add("modern-textfield");

        TextField yearField = new TextField();
        yearField.setPromptText("e.g., 2020-2024 or May 2024");
        yearField.getStyleClass().add("modern-textfield");

        TextField gpaField = new TextField();
        gpaField.setPromptText("e.g., 3.8/4.0 (optional)");
        gpaField.getStyleClass().add("modern-textfield");

        grid.add(new Label("Degree/Certification:"), 0, 0);
        grid.add(degreeField, 1, 0);
        grid.add(new Label("Institution:"), 0, 1);
        grid.add(institutionField, 1, 1);
        grid.add(new Label("Year:"), 0, 2);
        grid.add(yearField, 1, 2);
        grid.add(new Label("GPA:"), 0, 3);
        grid.add(gpaField, 1, 3);

        container.getChildren().addAll(header, grid);

        // Create controller for this education item
        EducationItemController controller = new EducationItemController(degreeField, institutionField, yearField, gpaField);
        educationControllers.add(controller);

        return container;
    }

    private void updateEducationNumbers() {
        for (int i = 0; i < educationContainer.getChildren().size(); i++) {
            VBox educationItem = (VBox) educationContainer.getChildren().get(i);
            HBox header = (HBox) educationItem.getChildren().get(0);
            Label headerLabel = (Label) header.getChildren().get(0);
            headerLabel.setText("Education #" + (i + 1));
        }
    }

    @FXML
    private void addExperience() {
        try {
            VBox experienceItem = createExperienceItem();
            experienceContainer.getChildren().add(experienceItem);
            updateStatus("Work experience entry added");
        } catch (Exception e) {
            showAlert("Error", "Failed to add experience entry: " + e.getMessage());
        }
    }

    private VBox createExperienceItem() {
        VBox container = new VBox(12);
        container.getStyleClass().add("experience-item");

        // Header with remove button
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label headerLabel = new Label("Experience #" + (experienceControllers.size() + 1));
        headerLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button removeBtn = new Button("✖ Remove");
        removeBtn.getStyleClass().add("remove-button");
        removeBtn.setOnAction(e -> {
            experienceContainer.getChildren().remove(container);
            updateExperienceNumbers();
            updateStatus("Work experience entry removed");
        });

        header.getChildren().addAll(headerLabel, spacer, removeBtn);

        // Form fields
        GridPane basicInfo = new GridPane();
        basicInfo.setHgap(15);
        basicInfo.setVgap(10);

        TextField jobTitleField = new TextField();
        jobTitleField.setPromptText("e.g., Software Engineer");
        jobTitleField.getStyleClass().add("modern-textfield");

        TextField companyField = new TextField();
        companyField.setPromptText("e.g., Tech Solutions Inc.");
        companyField.getStyleClass().add("modern-textfield");

        TextField locationField = new TextField();
        locationField.setPromptText("e.g., New York, NY");
        locationField.getStyleClass().add("modern-textfield");

        TextField durationField = new TextField();
        durationField.setPromptText("e.g., Jan 2022 - Present");
        durationField.getStyleClass().add("modern-textfield");

        basicInfo.add(new Label("Job Title:"), 0, 0);
        basicInfo.add(jobTitleField, 1, 0);
        basicInfo.add(new Label("Company:"), 2, 0);
        basicInfo.add(companyField, 3, 0);
        basicInfo.add(new Label("Location:"), 0, 1);
        basicInfo.add(locationField, 1, 1);
        basicInfo.add(new Label("Duration:"), 2, 1);
        basicInfo.add(durationField, 3, 1);

        // Achievements/Responsibilities
        Label achievementsLabel = new Label("Key Achievements & Responsibilities:");
        achievementsLabel.setStyle("-fx-font-weight: 500; -fx-padding: 10 0 5 0;");

        TextArea achievementsArea = new TextArea();
        achievementsArea.setPromptText("• Developed and maintained web applications using React and Node.js\n• Led a team of 3 developers to deliver projects 20% ahead of schedule\n• Implemented automated testing that reduced bugs by 40%");
        achievementsArea.setPrefRowCount(4);
        achievementsArea.setWrapText(true);
        achievementsArea.getStyleClass().add("modern-textarea");

        container.getChildren().addAll(header, basicInfo, achievementsLabel, achievementsArea);

        // Create controller for this experience item
        ExperienceItemController controller = new ExperienceItemController(
                jobTitleField, companyField, locationField, durationField, achievementsArea);
        experienceControllers.add(controller);

        return container;
    }

    private void updateExperienceNumbers() {
        for (int i = 0; i < experienceContainer.getChildren().size(); i++) {
            VBox experienceItem = (VBox) experienceContainer.getChildren().get(i);
            HBox header = (HBox) experienceItem.getChildren().getFirst();
            Label headerLabel = (Label) header.getChildren().getFirst();
            headerLabel.setText("Experience #" + (i + 1));
        }
    }

    @FXML
    private void previewResume() {
        try {
            Resume resume = buildResumeFromForm();
            ResumePreviewWindow.show(resume, isDarkTheme);
            updateStatus("Resume preview opened");
        } catch (Exception e) {
            showAlert("Error", "Failed to preview resume: " + e.getMessage());
        }
    }

    @FXML
    private void generateResume() {
        // Validate input
        if (!validateForm()) {
            return;
        }

        try {
            Resume resume = buildResumeFromForm();
            ResumeGenerator.generatePDF(resume);

            showAlert("Success", "Resume PDF generated successfully!\nCheck your project directory for Resume.pdf");
            updateStatus("Resume PDF generated successfully");
        } catch (Exception e) {
            showAlert("Error", "Failed to generate PDF: " + e.getMessage());
            updateStatus("Error generating resume");
        }
    }

    @FXML
    private void saveTemplate() {
        try {
            Resume resume = buildResumeFromForm();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Resume Template");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("JSON Files", "*.json"));

            Stage stage = (Stage) nameField.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                ResumeTemplateManager.saveTemplate(resume, file);
                showAlert("Success", "Template saved successfully!");
                updateStatus("Template saved to " + file.getName());
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to save template: " + e.getMessage());
        }
    }

    @FXML
    private void loadTemplate() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Resume Template");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("JSON Files", "*.json"));

            Stage stage = (Stage) nameField.getScene().getWindow();
            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                Resume resume = ResumeTemplateManager.loadTemplate(file);
                populateFormFromResume(resume);
                showAlert("Success", "Template loaded successfully!");
                updateStatus("Template loaded from " + file.getName());
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load template: " + e.getMessage());
        }
    }

    private boolean validateForm() {
        if (isEmpty(nameField.getText())) {
            showAlert("Validation Error", "Please enter your name");
            nameField.requestFocus();
            return false;
        }

        if (isEmpty(emailField.getText())) {
            showAlert("Validation Error", "Please enter your email address");
            emailField.requestFocus();
            return false;
        }

        if (!isValidEmail(emailField.getText())) {
            showAlert("Validation Error", "Please enter a valid email address");
            emailField.requestFocus();
            return false;
        }

        if (isEmpty(summaryArea.getText())) {
            showAlert("Validation Error", "Please enter a professional summary");
            summaryArea.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private Resume buildResumeFromForm() {
        Resume resume = new Resume();

        // Basic information
        resume.setName(nameField.getText().trim());
        resume.setEmail(emailField.getText().trim());
        resume.setPhoneNumber(phoneField.getText() != null ? phoneField.getText().trim() : "");
        resume.setLocation(locationField.getText() != null ? locationField.getText().trim() : "");
        resume.setLinkedIn(linkedInField.getText() != null ? linkedInField.getText().trim() : "");
        resume.setProfessionalSummary(summaryArea.getText() != null ? summaryArea.getText().trim() : "");
        resume.setSkills(skillsArea.getText() != null ? skillsArea.getText().trim() : "");

        // Education
        List<Education> educationList = new ArrayList<>();
        for (EducationItemController controller : educationControllers) {
            Education edu = controller.getEducation();
            if (edu != null) {
                educationList.add(edu);
            }
        }
        resume.setEducationList(educationList);

        // Work Experience
        List<WorkExperience> workExperiences = new ArrayList<>();
        for (ExperienceItemController controller : experienceControllers) {
            WorkExperience exp = controller.getWorkExperience();
            if (exp != null) {
                workExperiences.add(exp);
            }
        }
        resume.setWorkExperiences(workExperiences);

        return resume;
    }

    private void populateFormFromResume(Resume resume) {
        // Clear existing dynamic items
        educationContainer.getChildren().clear();
        experienceContainer.getChildren().clear();
        educationControllers.clear();
        experienceControllers.clear();

        // Basic information
        nameField.setText(resume.getName() != null ? resume.getName() : "");
        emailField.setText(resume.getEmail() != null ? resume.getEmail() : "");
        phoneField.setText(resume.getPhoneNumber() != null ? resume.getPhoneNumber() : "");
        locationField.setText(resume.getLocation() != null ? resume.getLocation() : "");
        linkedInField.setText(resume.getLinkedIn() != null ? resume.getLinkedIn() : "");
        summaryArea.setText(resume.getProfessionalSummary() != null ? resume.getProfessionalSummary() : "");
        skillsArea.setText(resume.getSkills() != null ? resume.getSkills() : "");

        // Education
        if (resume.getEducationList() != null && !resume.getEducationList().isEmpty()) {
            for (Education edu : resume.getEducationList()) {
                VBox educationItem = createEducationItem();
                EducationItemController controller = educationControllers.getLast();
                controller.setEducation(edu);
            }
        } else {
            addEducation(); // Add at least one
        }

        // Work Experience
        if (resume.getWorkExperiences() != null && !resume.getWorkExperiences().isEmpty()) {
            for (WorkExperience exp : resume.getWorkExperiences()) {
                VBox experienceItem = createExperienceItem();
                ExperienceItemController controller = experienceControllers.getLast();
                controller.setWorkExperience(exp);
            }
        } else {
            addExperience(); // Add at least one
        }
    }

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Apply theme to dialog
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().clear();

        String cssPath = isDarkTheme ? "/styles/dark_theme.css" : "/styles/light_theme.css";
        URL resourceUrl = getClass().getResource(cssPath);

        if (resourceUrl != null) {
            dialogPane.getStylesheets().add(resourceUrl.toExternalForm());
        } else {
            System.err.println("Warning: CSS file " + cssPath + " not found! Dialog will use default styling.");
        }

        alert.showAndWait();
    }


    // Inner classes for managing dynamic form items
    private static class EducationItemController {
        private TextField degreeField;
        private TextField institutionField;
        private TextField yearField;
        private TextField gpaField;

        public EducationItemController(TextField degreeField, TextField institutionField,
                                       TextField yearField, TextField gpaField) {
            this.degreeField = degreeField;
            this.institutionField = institutionField;
            this.yearField = yearField;
            this.gpaField = gpaField;
        }

        public Education getEducation() {
            if (isEmpty(degreeField.getText()) && isEmpty(institutionField.getText())) {
                return null;
            }

            return new Education(
                    degreeField.getText() != null ? degreeField.getText().trim() : "",
                    institutionField.getText() != null ? institutionField.getText().trim() : "",
                    yearField.getText() != null ? yearField.getText().trim() : "",
                    gpaField.getText() != null ? gpaField.getText().trim() : ""
            );
        }

        public void setEducation(Education education) {
            degreeField.setText(education.getDegree());
            institutionField.setText(education.getInstitution());
            yearField.setText(education.getYear());
            gpaField.setText(education.getGpa());
        }

        private boolean isEmpty(String text) {
            return text == null || text.trim().isEmpty();
        }
    }

    private static class ExperienceItemController {
        private TextField jobTitleField;
        private TextField companyField;
        private TextField locationField;
        private TextField durationField;
        private TextArea achievementsArea;

        public ExperienceItemController(TextField jobTitleField, TextField companyField,
                                        TextField locationField, TextField durationField,
                                        TextArea achievementsArea) {
            this.jobTitleField = jobTitleField;
            this.companyField = companyField;
            this.locationField = locationField;
            this.durationField = durationField;
            this.achievementsArea = achievementsArea;
        }

        public WorkExperience getWorkExperience() {
            if (isEmpty(jobTitleField.getText()) && isEmpty(companyField.getText())) {
                return null;
            }

            WorkExperience exp = new WorkExperience(
                    jobTitleField.getText() != null ? jobTitleField.getText().trim() : "",
                    companyField.getText() != null ? companyField.getText().trim() : "",
                    locationField.getText() != null ? locationField.getText().trim() : "",
                    durationField.getText() != null ? durationField.getText().trim() : ""
            );

            // Parse achievements from text area
            String achievementsText = achievementsArea.getText();
            if (achievementsText != null && !achievementsText.trim().isEmpty()) {
                List<String> achievements = new ArrayList<>();
                String[] lines = achievementsText.split("\n");
                for (String line : lines) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        // Remove bullet points if present
                        if (line.startsWith("•") || line.startsWith("-") || line.startsWith("*")) {
                            line = line.substring(1).trim();
                        }
                        achievements.add(line);
                    }
                }
                exp.setAchievements(achievements);
            }

            return exp;
        }

        public void setWorkExperience(WorkExperience experience) {
            jobTitleField.setText(experience.getJobTitle());
            companyField.setText(experience.getCompany());
            locationField.setText(experience.getLocation());
            durationField.setText(experience.getDuration());

            // Set achievements
            if (experience.getAchievements() != null && !experience.getAchievements().isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String achievement : experience.getAchievements()) {
                    sb.append("• ").append(achievement).append("\n");
                }
                achievementsArea.setText(sb.toString().trim());
            }
        }

        private boolean isEmpty(String text) {
            return text == null || text.trim().isEmpty();
        }
    }
}