package com.basejava.webapp;

import com.basejava.webapp.model.*;

import java.io.File;
import java.time.LocalDate;

public class ResumeTestData {
    public static void main(String[] args) {
        // Task 7. Filling sections with data.
        //Create new Resume with name
        Resume resume = new Resume("Tom Jobs");
        System.out.println(resume.getFullName() + "\n");

        //Contacts
        resume.addContact(ContactType.PHONE, "+1234567890");
        resume.addContact(ContactType.SKYPE, "live:abc@gmail.com");
        resume.addContact(ContactType.EMAIL, "abc@gmail.com");
        resume.addContact(ContactType.LINKEDIN, "profile LINKEDIN");
        resume.addContact(ContactType.GITHUB, "profile GitHub");
        resume.addContact(ContactType.STACKOVERFLOW, "profile stackoverflow");
        resume.addContact(ContactType.URL_HOMEPAGE, "http://abc.ru");

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
        resume.addSection(SectionType.OBJECTIVE, objective);
        System.out.println("--Objective (class section: TextSection): \n"
                + resume.getSection(SectionType.OBJECTIVE) + "\n");

        //PERSONAL enum Section
        Section personal = new TextSection("text text text text text");
        resume.addSection(SectionType.PERSONAL, personal);
        System.out.println("--Personal (class section: TextSection): \n"
                + resume.getSection(SectionType.PERSONAL) + "\n");

        //ACHIEVEMENT enum Section
        ListSection achievement = new ListSection("text 1", "text 2", "text 3");
        resume.addSection(SectionType.ACHIEVEMENT, achievement);
        System.out.println("--Achievement (class section: ListSection): \n"
                + resume.getSection(SectionType.ACHIEVEMENT));

        //QUALIFICATION enum Section
        Section qualification = new ListSection("text 1", "text 2", "text 3");
        resume.addSection(SectionType.QUALIFICATION, qualification);
        System.out.println("--Qualification (class section: ListSection): \n"
                + resume.getSection(SectionType.QUALIFICATION) + "\n");

        //EXPERIENCE enum Section
        String nameCompany = "Air";
        String webSite = "http://air.com";
        String title = "some title of experience";
        LocalDate startDate = LocalDate.parse("2023-02-20");
        LocalDate endDate = LocalDate.parse("2023-02-21");
        String description = "Some text of description";
        CompanySection experience = new CompanySection(nameCompany, webSite, title, startDate, endDate, description);
        resume.addSection(SectionType.EXPERIENCE, experience);
        System.out.println("--Experience (class section: CompanySection): \n"
                + resume.getSection(SectionType.EXPERIENCE) + "\n");

        //EDUCATION enum Section
        CompanySection education = new CompanySection(nameCompany, webSite, title, startDate, endDate);
        resume.addSection(SectionType.EDUCATION, education);
        System.out.println("--Education (class section: CompanySection): \n"
                + resume.getSection(SectionType.EDUCATION));

        System.out.println();
        //------------------------------------------------------------------------------------------
        // Task 8. Recursively traversing and displaying filenames in directories and subdirectories
        // (root directory is my project)
        File srcDirectory = new File("./src");
        File basejavaProjectDirectory = new File("/Users/jaroslavyar/javaops/basejava");
        outputNamesFilesProject(srcDirectory);
    }

    static int countWhitespace = -1;

    private static void outputNamesFilesProject(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            assert files != null;

            if (files.length == 1 && files[0].isDirectory()) {
                countWhitespace += 2;
            }
            String formatDirectory = String.format("%" + countWhitespace + "s", "");
            System.out.printf("%s%-10s\n", formatDirectory, file.getName());

            for (File currentFile : files) {
                outputNamesFilesProject(currentFile);
            }
        } else {
            String formatFile = String.format("%" + (countWhitespace + 3) + "s", "");
            System.out.printf("%s%-10s\n", formatFile, file.getName());
        }
    }

    public static Resume createResumeWithoutContactAndSection(String uuid, String fullName) {
        return new Resume(uuid, fullName);
    }

    // Task 8. Method for test class AbstractStorageTest.
    public static Resume createResume(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);

        //Contacts adding
        resume.addContact(ContactType.PHONE, "+1234567890");
        resume.addContact(ContactType.SKYPE, "live:abc@gmail.com");
        resume.addContact(ContactType.EMAIL, "abc@gmail.com");
        resume.addContact(ContactType.LINKEDIN, "https://www.linkedin.com");
        resume.addContact(ContactType.GITHUB, "https://github.com");
        resume.addContact(ContactType.STACKOVERFLOW, "https://stackoverflow.com");
        resume.addContact(ContactType.URL_HOMEPAGE, "https://google.com");

        //OBJECTIVE enum Section
        Section objective = new TextSection("text for OBJECTIVE section");
        resume.addSection(SectionType.OBJECTIVE, objective);

        //PERSONAL enum Section
        Section personal = new TextSection("text for PERSONAL section");
        resume.addSection(SectionType.PERSONAL, personal);

        //ACHIEVEMENT enum Section
        ListSection achievement = new ListSection("text 1", "text 2", "text 3");
        resume.addSection(SectionType.ACHIEVEMENT, achievement);

        //QUALIFICATION enum Section
        Section qualification = new ListSection("text 10", "text 20", "text 30");
        resume.addSection(SectionType.QUALIFICATION, qualification);

        //EXPERIENCE enum Section
        String nameCompany = "Air";
        String webSite = "http://air.com";
        String title = "some title of experience";
        LocalDate startDate = LocalDate.parse("2023-02-20");
        LocalDate endDate = LocalDate.parse("2023-02-21");
        String description = "Some text of description";
        CompanySection experience = new CompanySection(nameCompany, webSite, title, startDate, endDate, description);
        resume.addSection(SectionType.EXPERIENCE, experience);

        //EDUCATION enum Section
        CompanySection education = new CompanySection(nameCompany, webSite, title, startDate, endDate);
        resume.addSection(SectionType.EDUCATION, education);

        return resume;
    }
}