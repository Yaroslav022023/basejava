package com.basejava.webapp;

import com.basejava.webapp.model.*;

public class ResumeTestData {
    public static void main(String[] args) {
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
        System.out.println("---------------------------");

        //OBJECTIVE enumSection
        Section objective = new TextSection("text text text text text");
        resume.addSections(Sections.OBJECTIVE, objective);
        System.out.println("Objective (class section: TextSection): \n"
                + resume.getSection(Sections.OBJECTIVE) + "\n");

        //PERSONAL enumSection
        Section personal = new TextSection("text text text text text");
        resume.addSections(Sections.PERSONAL, personal);
        System.out.println("Personal (class section: TextSection): \n"
                + resume.getSection(Sections.PERSONAL) + "\n");

        //ACHIEVEMENT enumSection
        Section achievement = new ListSection("text 1", "text 2", "text 3");
        resume.addSections(Sections.ACHIEVEMENT, achievement);
        System.out.println("Achievement (class section: ListSection): \n"
                + resume.getSection(Sections.ACHIEVEMENT) + "\n");

        //QUALIFICATION enumSection
        Section qualification = new ListSection("text 1", "text 2", "text 3");
        resume.addSections(Sections.QUALIFICATION, qualification);
        System.out.println("Qualification (class section: ListSection): \n"
                + resume.getSection(Sections.QUALIFICATION) + "\n");

        //EXPERIENCE enumSection
        String nameCompany = "Air";
        String webSite = "http://air.com";
        String title = "some title of experience";
        String startDate = "20.02.2023";
        String endDate = "21.02.2023";
        String description = "Some text of description";
        CompanySection experience = new CompanySection(nameCompany, webSite, title, startDate, endDate, description);
        resume.addSections(Sections.EXPERIENCE, experience);
        System.out.println("Experience (class section: CompanySection): \n"
                + resume.getSection(Sections.EXPERIENCE) + "\n");

        //EDUCATION enumSection
        CompanySection education = new CompanySection(nameCompany, webSite, title, startDate, endDate);
        resume.addSections(Sections.EDUCATION, education);
        System.out.println("Education (class section: CompanySection): \n"
                + resume.getSection(Sections.EDUCATION));

        //adding additional Period into exist Company (variable 'education')
        String title2 = "additional title of experience";
        String startDate2 = "05.03.2023";
        String endDate2 = "06.03.2023";
        education.getCompany(nameCompany).addPeriod(title2, startDate2, endDate2);
        System.out.println("adding additional Period into exist Company (variable 'education'):"
                + "\n" + resume.getSection(Sections.EDUCATION));

        System.out.println("---------------------------");
    }
}