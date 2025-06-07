package com.example.resumeportfolio;

import java.util.List;
import java.util.ArrayList;

public class Resume {
    private String name;
    private String email;
    private String phoneNumber;
    private String location;
    private String linkedIn;
    private String professionalSummary;
    private String education;
    private String experience;
    private String skills;
    private List<WorkExperience> workExperiences;
    private List<String> coreSkills;
    private List<Education> educationList;

    // Default constructor
    public Resume() {
        this.workExperiences = new ArrayList<>();
        this.coreSkills = new ArrayList<>();
        this.educationList = new ArrayList<>();
    }

    // Legacy constructor for backward compatibility
    public Resume(String name, String email, String phoneNumber, String education, String experience, String skills) {
        this();
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.education = education;
        this.experience = experience;
        this.skills = skills;
    }

    // Enhanced constructor
    public Resume(String name, String email, String phoneNumber, String location, String linkedIn,
                  String professionalSummary, String education, String experience, String skills) {
        this();
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.linkedIn = linkedIn;
        this.professionalSummary = professionalSummary;
        this.education = education;
        this.experience = experience;
        this.skills = skills;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getLinkedIn() { return linkedIn; }
    public void setLinkedIn(String linkedIn) { this.linkedIn = linkedIn; }

    public String getProfessionalSummary() { return professionalSummary; }
    public void setProfessionalSummary(String professionalSummary) { this.professionalSummary = professionalSummary; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public List<WorkExperience> getWorkExperiences() { return workExperiences; }
    public void setWorkExperiences(List<WorkExperience> workExperiences) { this.workExperiences = workExperiences; }

    public List<String> getCoreSkills() { return coreSkills; }
    public void setCoreSkills(List<String> coreSkills) { this.coreSkills = coreSkills; }

    public List<Education> getEducationList() { return educationList; }
    public void setEducationList(List<Education> educationList) { this.educationList = educationList; }

    // Inner classes for structured data
    public static class WorkExperience {
        private String jobTitle;
        private String company;
        private String location;
        private String duration;
        private List<String> achievements;

        public WorkExperience(String jobTitle, String company, String location, String duration) {
            this.jobTitle = jobTitle;
            this.company = company;
            this.location = location;
            this.duration = duration;
            this.achievements = new ArrayList<>();
        }

        // Getters and Setters
        public String getJobTitle() { return jobTitle; }
        public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

        public String getCompany() { return company; }
        public void setCompany(String company) { this.company = company; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }

        public String getDuration() { return duration; }
        public void setDuration(String duration) { this.duration = duration; }

        public List<String> getAchievements() { return achievements; }
        public void setAchievements(List<String> achievements) { this.achievements = achievements; }
    }

    public static class Education {
        private String degree;
        private String institution;
        private String year;
        private String gpa;

        public Education(String degree, String institution, String year, String gpa) {
            this.degree = degree;
            this.institution = institution;
            this.year = year;
            this.gpa = gpa;
        }

        // Getters and Setters
        public String getDegree() { return degree; }
        public void setDegree(String degree) { this.degree = degree; }

        public String getInstitution() { return institution; }
        public void setInstitution(String institution) { this.institution = institution; }

        public String getYear() { return year; }
        public void setYear(String year) { this.year = year; }

        public String getGpa() { return gpa; }
        public void setGpa(String gpa) { this.gpa = gpa; }
    }
}