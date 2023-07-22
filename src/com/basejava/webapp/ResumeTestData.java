package com.basejava.webapp;

import com.basejava.webapp.model.*;

import java.io.File;

public class ResumeTestData {
    public static void main(String[] args) {
        // Task 7. Filling sections with data.
        //Create new Resume with name
        Resume resume = new Resume("Tom Jobs");
        System.out.println(resume.getFullName() + "\n");

        //Contacts
        resume.addContacts(ContactType.PHONE, "+1234567890");
        resume.addContacts(ContactType.SKYPE, "live:abc@gmail.com");
        resume.addContacts(ContactType.EMAIL, "abc@gmail.com");
        resume.addContacts(ContactType.LINKEDIN, "profile LINKEDIN");
        resume.addContacts(ContactType.GITHUB, "profile GitHub");
        resume.addContacts(ContactType.STACKOVERFLOW, "profile stackoverflow");
        resume.addContacts(ContactType.URL_HOMEPAGE, "http://abc.ru");

        System.out.println(resume.getContact(ContactType.PHONE));
        System.out.println(resume.getContact(ContactType.SKYPE));
        System.out.println(resume.getContact(ContactType.EMAIL));
        System.out.println(resume.getContact(ContactType.LINKEDIN));
        System.out.println(resume.getContact(ContactType.GITHUB));
        System.out.println(resume.getContact(ContactType.STACKOVERFLOW));
        System.out.println(resume.getContact(ContactType.URL_HOMEPAGE));
        System.out.println();

        //OBJECTIVE enum Section
        Section objective = new TextSection("text text text text text");
        resume.addSections(SectionType.OBJECTIVE, objective);
        System.out.println("--Objective (class section: TextSection): \n"
                + resume.getSection(SectionType.OBJECTIVE) + "\n");

        //PERSONAL enum Section
        Section personal = new TextSection("text text text text text");
        resume.addSections(SectionType.PERSONAL, personal);
        System.out.println("--Personal (class section: TextSection): \n"
                + resume.getSection(SectionType.PERSONAL) + "\n");

        //ACHIEVEMENT enum Section
        ListSection achievement = new ListSection("text 1", "text 2", "text 3");
        resume.addSections(SectionType.ACHIEVEMENT, achievement);
        System.out.println("--Achievement (class section: ListSection): \n"
                + resume.getSection(SectionType.ACHIEVEMENT));

        //QUALIFICATION enum Section
        Section qualification = new ListSection("text 1", "text 2", "text 3");
        resume.addSections(SectionType.QUALIFICATION, qualification);
        System.out.println("--Qualification (class section: ListSection): \n"
                + resume.getSection(SectionType.QUALIFICATION) + "\n");

        //EXPERIENCE enum Section
        String nameCompany = "Air";
        String webSite = "http://air.com";
        String title = "some title of experience";
        String startDate = "20.02.2023";
        String endDate = "21.02.2023";
        String description = "Some text of description";
        CompanySection experience = new CompanySection(nameCompany, webSite, title, startDate, endDate, description);
        resume.addSections(SectionType.EXPERIENCE, experience);
        System.out.println("--Experience (class section: CompanySection): \n"
                + resume.getSection(SectionType.EXPERIENCE) + "\n");

        //EDUCATION enum Section
        CompanySection education = new CompanySection(nameCompany, webSite, title, startDate, endDate);
        resume.addSections(SectionType.EDUCATION, education);
        System.out.println("--Education (class section: CompanySection): \n"
                + resume.getSection(SectionType.EDUCATION));

        //------------------------------------------------------------------------------------------
        // Task 8. Recursively traversing and displaying filenames in directories and subdirectories
        // (root directory is my project)
        File srcDirectory = new File("./src");
        File basejavaProjectDirectory = new File("/Users/jaroslavyar/javaops/basejava");
        outputNamesFilesProject(srcDirectory);
    }

    private static void outputNamesFilesProject(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();

            assert files != null;
            for (File currentFile : files) {
                outputNamesFilesProject(currentFile);
            }
        } else {
            System.out.println(file.getPath());
        }
    }

    // Task 8. Method for test class AbstractStorageTest.
    public static Resume createResume(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);

        //Contacts adding
        resume.addContacts(ContactType.PHONE, "+1234567890");
        resume.addContacts(ContactType.SKYPE, "live:abc@gmail.com");
        resume.addContacts(ContactType.EMAIL, "abc@gmail.com");
        resume.addContacts(ContactType.LINKEDIN, "profile LINKEDIN");
        resume.addContacts(ContactType.GITHUB, "profile GitHub");
        resume.addContacts(ContactType.STACKOVERFLOW, "profile stackoverflow");
        resume.addContacts(ContactType.URL_HOMEPAGE, "http://abc.ru");

        //OBJECTIVE enum Section
        Section objective = new TextSection("text text text text text");
        resume.addSections(SectionType.OBJECTIVE, objective);

        //PERSONAL enum Section
        Section personal = new TextSection("text text text text text");
        resume.addSections(SectionType.PERSONAL, personal);

        //ACHIEVEMENT enum Section
        ListSection achievement = new ListSection("text 1", "text 2", "text 3");
        resume.addSections(SectionType.ACHIEVEMENT, achievement);

        //QUALIFICATION enum Section
        Section qualification = new ListSection("text 10", "text 20", "text 30");
        resume.addSections(SectionType.QUALIFICATION, qualification);

        //EXPERIENCE enum Section
        String nameCompany = "Air";
        String webSite = "http://air.com";
        String title = "some title of experience";
        String startDate = "20.02.2023";
        String endDate = "21.02.2023";
        String description = "Some text of description";
        CompanySection experience = new CompanySection(nameCompany, webSite, title, startDate, endDate, description);
        resume.addSections(SectionType.EXPERIENCE, experience);

        //EDUCATION enum Section
        CompanySection education = new CompanySection(nameCompany, webSite, title, startDate, endDate);
        resume.addSections(SectionType.EDUCATION, education);

        return resume;
    }
}