package com.example.resumeportfolio;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Enhanced PDF generator for ATS-optimized resumes
 * Follows best practices for Applicant Tracking System compatibility
 */
public class ResumeGenerator {

    // ATS-friendly colors and fonts
    private static final Color PRIMARY_COLOR = new Color(51, 51, 51);      // Dark gray
    private static final Color ACCENT_COLOR = new Color(70, 130, 180);     // Steel blue
    private static final Color TEXT_COLOR = new Color(33, 37, 41);         // Near black
    private static final Color SECTION_COLOR = new Color(108, 117, 125);   // Medium gray

    // Font configurations for ATS compatibility
    private static final String FONT_NAME = "Helvetica";
    private static final int TITLE_SIZE = 24;
    private static final int SECTION_HEADER_SIZE = 14;
    private static final int CONTENT_SIZE = 11;
    private static final int SMALL_SIZE = 10;


    public static void generatePDF(Resume resume) throws DocumentException, IOException {
        // Open file chooser for user to select save location
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Resume As PDF");
        fileChooser.setSelectedFile(new File("Resume_" + (resume.getName() != null ?
                resume.getName().replaceAll("[^a-zA-Z0-9]", "_") : "Generated") + ".pdf"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection != JFileChooser.APPROVE_OPTION) {
            System.out.println("Resume PDF generation cancelled.");
            return; // Exit if the user cancels file selection
        }

        File selectedFile = fileChooser.getSelectedFile();
        String filePath = selectedFile.getAbsolutePath();

        // Create document with standard page size for ATS compatibility
        Document document = new Document(PageSize.LETTER, 50, 50, 50, 50);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        try {
            addDocumentMetadata(document, resume);
            addHeader(document, resume);
            addProfessionalSummary(document, resume);
            addCoreSkills(document, resume);
            addProfessionalExperience(document, resume);
            addEducation(document, resume);
        } finally {
            document.close();
        }

        System.out.println("ATS-optimized resume generated and saved to: " + filePath);
    }

    private static void addDocumentMetadata(Document document, Resume resume) {
        document.addTitle("Resume - " + (resume.getName() != null ? resume.getName() : "Professional"));
        document.addAuthor(resume.getName() != null ? resume.getName() : "");
        document.addSubject("Professional Resume");
        document.addKeywords("resume, professional, ATS-optimized");
        document.addCreator("Professional Resume Builder");
    }

    private static void addHeader(Document document, Resume resume) throws DocumentException {
        // Name - Large, bold, ATS-friendly
        Font nameFont = new Font(Font.HELVETICA, TITLE_SIZE, Font.BOLD, PRIMARY_COLOR);
        Paragraph name = new Paragraph(resume.getName() != null ? resume.getName() : "", nameFont);
        name.setAlignment(Element.ALIGN_CENTER);
        name.setSpacingAfter(8);
        document.add(name);

        // Contact information - Simple format for ATS parsing
        Font contactFont = new Font(Font.HELVETICA, CONTENT_SIZE, Font.NORMAL, TEXT_COLOR);
        StringBuilder contactInfo = new StringBuilder();

        if (resume.getLocation() != null && !resume.getLocation().trim().isEmpty()) {
            contactInfo.append(resume.getLocation());
        }

        if (resume.getEmail() != null && !resume.getEmail().trim().isEmpty()) {
            if (contactInfo.length() > 0) contactInfo.append(" • ");
            contactInfo.append(resume.getEmail());
        }

        if (resume.getPhoneNumber() != null && !resume.getPhoneNumber().trim().isEmpty()) {
            if (contactInfo.length() > 0) contactInfo.append(" • ");
            contactInfo.append(resume.getPhoneNumber());
        }

        if (resume.getLinkedIn() != null && !resume.getLinkedIn().trim().isEmpty()) {
            if (contactInfo.length() > 0) contactInfo.append(" • ");
            contactInfo.append(resume.getLinkedIn());
        }

        if (contactInfo.length() > 0) {
            Paragraph contact = new Paragraph(contactInfo.toString(), contactFont);
            contact.setAlignment(Element.ALIGN_CENTER);
            contact.setSpacingAfter(20);
            document.add(contact);
        }
    }

    private static void addProfessionalSummary(Document document, Resume resume) throws DocumentException {
        if (resume.getProfessionalSummary() == null || resume.getProfessionalSummary().trim().isEmpty()) {
            return;
        }

        addSectionHeader(document, "PROFESSIONAL SUMMARY");

        Font summaryFont = new Font(Font.HELVETICA, CONTENT_SIZE, Font.NORMAL, TEXT_COLOR);
        Paragraph summary = new Paragraph(resume.getProfessionalSummary().trim(), summaryFont);
        summary.setAlignment(Element.ALIGN_JUSTIFIED);
        summary.setSpacingAfter(15);
        document.add(summary);
    }

    private static void addCoreSkills(Document document, Resume resume) throws DocumentException {
        String skills = resume.getSkills();
        if (skills == null || skills.trim().isEmpty()) {
            return;
        }

        addSectionHeader(document, "CORE SKILLS");

        Font skillsFont = new Font(Font.HELVETICA, CONTENT_SIZE, Font.NORMAL, TEXT_COLOR);

        // Format skills in a clean, ATS-friendly way
        String[] skillArray = skills.split("[,;]");
        StringBuilder formattedSkills = new StringBuilder();

        for (int i = 0; i < skillArray.length; i++) {
            String skill = skillArray[i].trim();
            if (!skill.isEmpty()) {
                if (formattedSkills.length() > 0) {
                    formattedSkills.append(" • ");
                }
                formattedSkills.append(skill);
            }
        }

        Paragraph skillsParagraph = new Paragraph(formattedSkills.toString(), skillsFont);
        skillsParagraph.setSpacingAfter(15);
        document.add(skillsParagraph);
    }

    private static void addProfessionalExperience(Document document, Resume resume) throws DocumentException {
        List<Resume.WorkExperience> experiences = resume.getWorkExperiences();
        if (experiences == null || experiences.isEmpty()) {
            return;
        }

        addSectionHeader(document, "PROFESSIONAL EXPERIENCE");

        Font jobTitleFont = new Font(Font.HELVETICA, SECTION_HEADER_SIZE, Font.BOLD, PRIMARY_COLOR);
        Font companyFont = new Font(Font.HELVETICA, CONTENT_SIZE, Font.BOLD, TEXT_COLOR);
        Font detailFont = new Font(Font.HELVETICA, SMALL_SIZE, Font.NORMAL, SECTION_COLOR);
        Font achievementFont = new Font(Font.HELVETICA, CONTENT_SIZE, Font.NORMAL, TEXT_COLOR);

        for (Resume.WorkExperience exp : experiences) {
            if (exp.getJobTitle() == null || exp.getJobTitle().trim().isEmpty()) {
                continue;
            }

            // Job Title
            Paragraph jobTitle = new Paragraph(exp.getJobTitle().trim(), jobTitleFont);
            jobTitle.setSpacingAfter(4);
            document.add(jobTitle);

            // Company and Location
            StringBuilder companyInfo = new StringBuilder();
            if (exp.getCompany() != null && !exp.getCompany().trim().isEmpty()) {
                companyInfo.append(exp.getCompany().trim());
            }

            if (exp.getLocation() != null && !exp.getLocation().trim().isEmpty()) {
                if (companyInfo.length() > 0) companyInfo.append(", ");
                companyInfo.append(exp.getLocation().trim());
            }

            if (companyInfo.length() > 0) {
                Paragraph company = new Paragraph(companyInfo.toString(), companyFont);
                company.setSpacingAfter(2);
                document.add(company);
            }

            // Duration
            if (exp.getDuration() != null && !exp.getDuration().trim().isEmpty()) {
                Paragraph duration = new Paragraph(exp.getDuration().trim(), detailFont);
                duration.setSpacingAfter(8);
                document.add(duration);
            }

            // Achievements
            if (exp.getAchievements() != null && !exp.getAchievements().isEmpty()) {
                for (String achievement : exp.getAchievements()) {
                    if (achievement != null && !achievement.trim().isEmpty()) {
                        Paragraph bullet = new Paragraph("• " + achievement.trim(), achievementFont);
                        bullet.setIndentationLeft(20);
                        bullet.setSpacingAfter(4);
                        document.add(bullet);
                    }
                }
            }

            // Add spacing between experiences
            document.add(new Paragraph(" ", achievementFont));
        }
    }

    private static void addEducation(Document document, Resume resume) throws DocumentException {
        List<Resume.Education> educationList = resume.getEducationList();
        if (educationList == null || educationList.isEmpty()) {
            return;
        }

        addSectionHeader(document, "EDUCATION");

        Font degreeFont = new Font(Font.HELVETICA, CONTENT_SIZE, Font.BOLD, PRIMARY_COLOR);
        Font institutionFont = new Font(Font.HELVETICA, CONTENT_SIZE, Font.NORMAL, TEXT_COLOR);
        Font detailFont = new Font(Font.HELVETICA, SMALL_SIZE, Font.NORMAL, SECTION_COLOR);

        for (Resume.Education edu : educationList) {
            if (edu.getDegree() == null || edu.getDegree().trim().isEmpty()) {
                continue;
            }

            // Degree
            Paragraph degree = new Paragraph(edu.getDegree().trim(), degreeFont);
            degree.setSpacingAfter(4);
            document.add(degree);

            // Institution
            if (edu.getInstitution() != null && !edu.getInstitution().trim().isEmpty()) {
                Paragraph institution = new Paragraph(edu.getInstitution().trim(), institutionFont);
                institution.setSpacingAfter(2);
                document.add(institution);
            }

            // Year and GPA
            StringBuilder details = new StringBuilder();
            if (edu.getYear() != null && !edu.getYear().trim().isEmpty()) {
                details.append(edu.getYear().trim());
            }

            if (edu.getGpa() != null && !edu.getGpa().trim().isEmpty()) {
                if (details.length() > 0) details.append(" • ");
                details.append("GPA: ").append(edu.getGpa().trim());
            }

            if (details.length() > 0) {
                Paragraph detailsParagraph = new Paragraph(details.toString(), detailFont);
                detailsParagraph.setSpacingAfter(12);
                document.add(detailsParagraph);
            }
        }
    }

    private static void addSectionHeader(Document document, String headerText) throws DocumentException {
        Font headerFont = new Font(Font.HELVETICA, SECTION_HEADER_SIZE, Font.BOLD, ACCENT_COLOR);
        Paragraph header = new Paragraph(headerText, headerFont);
        header.setSpacingBefore(8);
        header.setSpacingAfter(10);

        // Add subtle line under section headers for better visual separation
        header.setSpacingAfter(8);
        document.add(header);

        // Add a thin line
        LineSeparator line = new LineSeparator(1, 100, ACCENT_COLOR, Element.ALIGN_LEFT, -2);
        document.add(new Chunk(line));
        document.add(new Paragraph(" ")); // Small spacing after line
    }
}
