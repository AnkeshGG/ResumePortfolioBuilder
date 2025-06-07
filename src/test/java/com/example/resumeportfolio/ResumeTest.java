package com.example.resumeportfolio;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Resume model.
 * This sample test uses an Indian name ("Rajesh Kumar") and sample work and education entries.
 */
public class ResumeTest {

    @Test
    public void testResumeData() {
        // Create and populate a Resume instance
        Resume resume = new Resume();
        resume.setName("Ankesh Kumar");
        resume.setEmail("ankesh.kumar@example.com");
        resume.setPhoneNumber("(080) 1234567890");
        resume.setLocation("Bengaluru, Karnataka, India");
        resume.setLinkedIn("linkedin.com/in/ankeshkumar");
        resume.setProfessionalSummary("Experienced software developer with 7 years of experience in developing robust Java applications.");
        resume.setSkills("Java, Spring, Hibernate, JavaFX, Maven");

        // Create a work experience sample entry.
        Resume.WorkExperience workExp = new Resume.WorkExperience("Senior Software Engineer", "Tech Innovators Inc.", "Bengaluru, India", "Jan 2025 - Present");
        // Assuming achievements as a list of strings
        workExp.setAchievements(List.of("Developed core Java services", "Led migration to a microservices architecture"));

        // Create an education sample entry.
        Resume.Education education = new Resume.Education("B.Tech in Computer Science", "KIIT, Bhubaneswar", "2023 - 2027", "9.2/10");

        // Set the work and education lists in the resume.
        resume.setWorkExperiences(List.of(workExp));
        resume.setEducationList(List.of(education));

        // ----- Begin Assertions -----
        // Validate basic information.
        assertEquals("Ankesh Kumar", resume.getName());
        assertEquals("ankesh.kumar@example.com", resume.getEmail());
        assertEquals("(080) 1234567890", resume.getPhoneNumber());
        assertEquals("Bengaluru, Karnataka, India", resume.getLocation());
        assertEquals("linkedin.com/in/ankeshkumar", resume.getLinkedIn());
        assertEquals("Experienced software developer with 7 years of experience in developing robust Java applications.", resume.getProfessionalSummary());
        assertEquals("Java, Spring, Hibernate, JavaFX, Maven", resume.getSkills());

        // Validate work experience.
        List<Resume.WorkExperience> workList = resume.getWorkExperiences();
        assertNotNull(workList, "Work experiences list should not be null");
        assertEquals(1, workList.size(), "There should be one work experience entry");
        Resume.WorkExperience retrievedExp = workList.get(0);
        assertEquals("Senior Software Engineer", retrievedExp.getJobTitle());
        assertEquals("Tech Innovators Inc.", retrievedExp.getCompany());
        assertEquals("Bengaluru, India", retrievedExp.getLocation());
        assertEquals("Jan 2025 - Present", retrievedExp.getDuration());
        assertNotNull(retrievedExp.getAchievements(), "Achievements should not be null");
        assertTrue(retrievedExp.getAchievements().contains("Developed core Java services"));
        assertTrue(retrievedExp.getAchievements().contains("Led migration to a microservices architecture"));

        // Validate education.
        List<Resume.Education> eduList = resume.getEducationList();
        assertNotNull(eduList, "Education list should not be null");
        assertEquals(1, eduList.size(), "There should be one education entry");
        Resume.Education retrievedEdu = eduList.get(0);
        assertEquals("B.Tech in Computer Science", retrievedEdu.getDegree());
        assertEquals("KIIT, Bhubaneswar", retrievedEdu.getInstitution());
        assertEquals("2023 - 2027", retrievedEdu.getYear());
        assertEquals("9.2/10", retrievedEdu.getGpa());
    }
}
