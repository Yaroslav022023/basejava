package com.basejava.webapp.model;

import com.basejava.webapp.exceptions.NotExistCompanyException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CompanySectionTest extends AbstractResumeTest {
    @Test
    void getCompanies() {
        //EXPERIENCE
        List<Company> expectedList = new ArrayList<>();
        expectedList.add(expectedCompany);

        assertEquals(expectedList, ((CompanySection) resume.getSection(SectionType.EXPERIENCE)).getCompanies());
    }

    @Test
    void getCompany() {
        assertEquals(expectedCompany,
                ((CompanySection) resume.getSection(SectionType.EXPERIENCE)).getCompany(nameCompany));
    }

    @Test
    void addCompany() {
        String nameCompanyExperience = "Bus";
        String webSite = "http://bus.com";
        String title = "Some title of experience";
        LocalDate startDate = LocalDate.parse("2021-05-25");
        LocalDate endDate = LocalDate.parse("2021-05-25");
        String description = "some text of description";

        //EXPERIENCE
        Company newCompany = new Company(nameCompanyExperience, webSite, title, startDate, endDate, description);
        ((CompanySection) resume.getSection(SectionType.EXPERIENCE)).addCompany(newCompany);

        assertGet(newCompany, SectionType.EXPERIENCE, nameCompanyExperience);
        assertSize(2, SectionType.EXPERIENCE);
    }

    @Test
    void removeCompany() {
        //EXPERIENCE
        ((CompanySection) resume.getSection(SectionType.EXPERIENCE)).removeCompany(nameCompany);
        assertThrows(NotExistCompanyException.class, () ->
                ((CompanySection) resume.getSection(SectionType.EXPERIENCE)).getCompany(nameCompany));
        assertSize(0, SectionType.EXPERIENCE);
    }

    @Test
    void removeNotExistCompany() {
        assertFalse(((CompanySection) resume.getSection(SectionType.EXPERIENCE)).removeCompany("Check"));
    }

    @Test
    void clear() {
        //EXPERIENCE
        ((CompanySection) resume.getSection(SectionType.EXPERIENCE)).clear();
        assertSize(0, SectionType.EXPERIENCE);
    }
}