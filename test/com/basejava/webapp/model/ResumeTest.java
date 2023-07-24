package com.basejava.webapp.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ResumeTest extends AbstractResumeTest{
    @Test
    void getUuid() {
        assertEquals("UUID_1", resume.getUuid());
    }

    @Test
    void getFullName() {
        assertEquals("Tom Single", resume.getFullName());
    }

    @Test
    void getAllContacts() {
        Map<ContactType, String> expected = new HashMap<>();
        expected.put(ContactType.PHONE, "+1234567890");
        expected.put(ContactType.SKYPE, "live:abc@gmail.com");
        expected.put(ContactType.EMAIL, "abc@gmail.com");
        expected.put(ContactType.LINKEDIN, "profile LINKEDIN");
        expected.put(ContactType.GITHUB, "profile GitHub");
        expected.put(ContactType.STACKOVERFLOW, "profile stackoverflow");
        expected.put(ContactType.URL_HOMEPAGE, "http://abc.ru");

        assertEquals(expected, resume.getAllContacts());
    }

    @Test
    void getAllSections() {
        Map<SectionType, Section> expected = new HashMap<>();
        expected.put(SectionType.OBJECTIVE, new TextSection("text text text text text"));
        expected.put(SectionType.PERSONAL, new TextSection("text text text text text"));
        expected.put(SectionType.ACHIEVEMENT, new ListSection("text 1", "text 2", "text 3"));
        expected.put(SectionType.QUALIFICATION, new ListSection("text 10", "text 20", "text 30"));
        expected.put(SectionType.EXPERIENCE, new CompanySection(nameCompany,
                webSite, title, startDate, endDate, description));
        expected.put(SectionType.EDUCATION, new CompanySection(nameCompany,
                webSite, title, startDate, endDate));

        assertEquals(expected, resume.getAllSections());
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

    @Test
    void getSection() {
        Map<SectionType, Section> expected = new HashMap<>();
        expected.put(SectionType.OBJECTIVE, new TextSection("text text text text text"));

        assertEquals(expected.get(SectionType.OBJECTIVE), resume.getSection(SectionType.OBJECTIVE));
    }

    @Test
    void changeContacts() {
        String newContact = "+110";
        Map<ContactType, String> expected = new HashMap<>();
        expected.put(ContactType.PHONE, "+1234567890");
        expected.replace(ContactType.PHONE, newContact);
        resume.changeContacts(ContactType.PHONE, newContact);

        assertEquals(expected.get(ContactType.PHONE), resume.getContact(ContactType.PHONE));
    }

    @Test
    void removeContacts() {
        Map<ContactType, String> expected = new HashMap<>();
        expected.put(ContactType.PHONE, "+1234567890");

        assertEquals(expected.get(ContactType.PHONE), resume.getContact(ContactType.PHONE));

        expected.remove(ContactType.PHONE);
        resume.removeContacts(ContactType.PHONE);

        assertEquals(expected.get(ContactType.PHONE), resume.getContact(ContactType.PHONE));
    }

    @Test
    void addContacts() {
        //All Contacts for object 'resume' already added.
    }

    @Test
    void addSections() {
        //All Sections for object 'resume' already added.
    }

    @Test
    void clearContacts() {
        resume.clearContacts();
        assertEquals(0, resume.getAllContacts().size());
    }

    @Test
    void clearSections() {
        resume.clearSections();
        assertEquals(0, resume.getAllSections().size());
    }
}