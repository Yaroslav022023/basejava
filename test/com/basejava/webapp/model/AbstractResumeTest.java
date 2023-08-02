package com.basejava.webapp.model;

import com.basejava.webapp.ResumeTestData;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractResumeTest {
    protected static final String UUID_1 = "UUID_1";
    protected static final String FULL_NAME = "Tom Single";
    protected Resume resume;
    protected final String nameCompany = "Air";
    protected final String webSite = "http://air.com";
    protected final String title = "some title of experience";
    protected final LocalDate startDate = LocalDate.parse("2023-02-20");
    protected final LocalDate endDate = LocalDate.parse("2023-02-21");
    protected final String description = "Some text of description";
    protected final Company expectedCompany = new Company(
            nameCompany, webSite, title, startDate, endDate, description);

    @BeforeEach
    void setUp() {
        resume = ResumeTestData.createResume(UUID_1, FULL_NAME);
    }

    protected void assertSize(int size, SectionType sectionType) {
        assertEquals(size, ((CompanySection) resume.getSection(sectionType)).getCompanies().size());
    }

    protected void assertGet(Company company, SectionType sectionType, String nameCompany) {
        assertEquals(company, ((CompanySection) resume.getSection(sectionType)).getCompany(nameCompany));
    }
}