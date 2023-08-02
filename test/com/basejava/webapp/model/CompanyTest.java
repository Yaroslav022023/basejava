package com.basejava.webapp.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompanyTest extends AbstractResumeTest {
    @Test
    void getName() {
        //EXPERIENCE
        assertEquals(nameCompany,
                ((CompanySection) resume.getSection(SectionType.EXPERIENCE)).getCompany(nameCompany).getName());
    }

    @Test
    void setName() {
        //EXPERIENCE
        String newName = "Google";
        ((CompanySection) resume.getSection(SectionType.EXPERIENCE)).getCompany(nameCompany).setName(newName);
        assertEquals(newName,
                ((CompanySection) resume.getSection(SectionType.EXPERIENCE)).getCompany(newName).getName());
    }

    @Test
    void getWebSite() {
        //EXPERIENCE
        assertEquals(webSite,
                ((CompanySection) resume.getSection(SectionType.EXPERIENCE)).getCompany(nameCompany).getWebSite());
    }

    @Test
    void setWebSite() {
        //EXPERIENCE
        String newWebSite = "http://google.com";
        ((CompanySection) resume.getSection(SectionType.EXPERIENCE)).getCompany(nameCompany).setWebSite(newWebSite);
        assertEquals(newWebSite,
                ((CompanySection) resume.getSection(SectionType.EXPERIENCE)).getCompany(nameCompany).getWebSite());
    }

    @Test
    void getPeriods() {
        //EXPERIENCE
        assertEquals(expectedCompany.getPeriods(),
                ((CompanySection) resume.getSection(SectionType.EXPERIENCE)).getCompany(nameCompany).getPeriods());
    }

    @Test
    void addPeriod() {
        String title = "New title";
        LocalDate startDate = LocalDate.parse("2023-09-01");
        LocalDate endDate = LocalDate.parse("2023-09-05");
        String description = "New description";

        //EXPERIENCE
        expectedCompany.addPeriod(title, startDate, endDate, description);
        ((CompanySection) resume.getSection(SectionType.EXPERIENCE)).getCompany(nameCompany)
                .addPeriod(title, startDate, endDate, description);
        assertEquals(expectedCompany.getPeriods(),
                ((CompanySection) resume.getSection(SectionType.EXPERIENCE)).getCompany(nameCompany).getPeriods());

        //EDUCATION
        Company expectedCompany2 = new Company(nameCompany, webSite, this.title, this.startDate, this.endDate);
        String title2 = "New title";
        LocalDate startDate2 = LocalDate.parse("2023-09-10");
        LocalDate endDate2 = LocalDate.parse("2023-09-15");
        expectedCompany2.addPeriod(title2, startDate2, endDate2);
        ((CompanySection) resume.getSection(SectionType.EDUCATION)).getCompany(nameCompany)
                .addPeriod(title2, startDate2, endDate2);

        assertEquals(expectedCompany2.getPeriods(),
                ((CompanySection) resume.getSection(SectionType.EDUCATION)).getCompany(nameCompany).getPeriods());
    }

    @Test
    void removePeriod() {
        //EXPERIENCE
        Company.Period period = expectedCompany.getPeriods().get(0);
        expectedCompany.removePeriod(period);
        ((CompanySection) resume.getSection(SectionType.EXPERIENCE)).getCompany(nameCompany).removePeriod(period);

        assertEquals(expectedCompany.getPeriods(),
                ((CompanySection) resume.getSection(SectionType.EXPERIENCE)).getCompany(nameCompany).getPeriods());
    }

    @Test
    void clear() {
        //EXPERIENCE
        assertSize(1, SectionType.EXPERIENCE);
        ((CompanySection) resume.getSection(SectionType.EXPERIENCE)).clear();
        assertSize(0, SectionType.EXPERIENCE);
    }
}