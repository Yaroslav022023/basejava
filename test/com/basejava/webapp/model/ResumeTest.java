package com.basejava.webapp.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ResumeTest extends AbstractResumeTest{
    @Test
    void assertAllSection() {
        //OBJECTIVE - TextSection
        TextSection objective = new TextSection("text text text text text");
        assertEquals(objective, resume.getSection(SectionType.OBJECTIVE));

        //PERSONAL - TextSection
        TextSection personal = new TextSection("text text text text text");
        assertEquals(personal, resume.getSection(SectionType.PERSONAL));

        //ACHIEVEMENT - ListSection
        ListSection achievement = new ListSection("text 1", "text 2", "text 3");
        assertEquals(achievement, resume.getSection(SectionType.ACHIEVEMENT));

        //QUALIFICATION - ListSection
        ListSection qualification = new ListSection("text 10", "text 20", "text 30");
        assertEquals(qualification, resume.getSection(SectionType.QUALIFICATION));

        //EXPERIENCE - CompanySection
        CompanySection experience = new CompanySection(nameCompany, webSite, title, startDate, endDate, description);
        assertEquals(experience, resume.getSection(SectionType.EXPERIENCE));

        //EDUCATION - CompanySection
        CompanySection education = new CompanySection(nameCompany, webSite, title, startDate, endDate);
        assertEquals(education, resume.getSection(SectionType.EDUCATION));
    }

    @Test
    void getContact() {
        assertEquals("+1234567890", resume.getContact(ContactType.PHONE));
        assertEquals("live:abc@gmail.com", resume.getContact(ContactType.SKYPE));
        assertEquals("abc@gmail.com", resume.getContact(ContactType.EMAIL));
        assertEquals("profile LINKEDIN", resume.getContact(ContactType.LINKEDIN));
        assertEquals("profile GitHub", resume.getContact(ContactType.GITHUB));
        assertEquals("profile stackoverflow", resume.getContact(ContactType.STACKOVERFLOW));
        assertEquals("http://abc.ru", resume.getContact(ContactType.URL_HOMEPAGE));
    }

    @Test
    void notExistContacts() {
        assertNotEquals("+12345678901", resume.getContact(ContactType.PHONE));
        assertNotEquals("http://google.com", resume.getContact(ContactType.URL_HOMEPAGE));
    }
}