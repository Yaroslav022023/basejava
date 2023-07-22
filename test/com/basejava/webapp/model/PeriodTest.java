package com.basejava.webapp.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PeriodTest extends AbstractResumeTest {
    @Test
    void getTittle() {
        //EXPERIENCE
        assertEquals(expectedCompany.getPeriods().get(0).getTittle(),
                ((CompanySection) resume.getSection(SectionType.EXPERIENCE))
                        .getCompany(nameCompany).getPeriods().get(0).getTittle());
    }

    @Test
    void setTittle() {
        //EXPERIENCE
        String newTitle = "new Title";
        expectedCompany.getPeriods().get(0).setTittle(newTitle);
        ((CompanySection) resume.getSection(SectionType.EXPERIENCE))
                .getCompany(nameCompany).getPeriods().get(0).setTittle(newTitle);

        assertEquals(expectedCompany.getPeriods().get(0).getTittle(),
                ((CompanySection) resume.getSection(SectionType.EXPERIENCE))
                        .getCompany(nameCompany).getPeriods().get(0).getTittle());
    }

    @Test
    void getStartDate() {
        //EXPERIENCE
        assertEquals(expectedCompany.getPeriods().get(0).getStartDate(),
                ((CompanySection) resume.getSection(SectionType.EXPERIENCE))
                        .getCompany(nameCompany).getPeriods().get(0).getStartDate());
    }

    @Test
    void setStartDate() {
        //EXPERIENCE
        String newStartDate = "11.11.2023";
        expectedCompany.getPeriods().get(0).setStartDate(newStartDate);
        ((CompanySection) resume.getSection(SectionType.EXPERIENCE))
                .getCompany(nameCompany).getPeriods().get(0).setStartDate(newStartDate);

        assertEquals(expectedCompany.getPeriods().get(0).getStartDate(),
                ((CompanySection) resume.getSection(SectionType.EXPERIENCE))
                        .getCompany(nameCompany).getPeriods().get(0).getStartDate());
    }

    @Test
    void getEndDate() {
        //EXPERIENCE
        assertEquals(expectedCompany.getPeriods().get(0).getEndDate(),
                ((CompanySection) resume.getSection(SectionType.EXPERIENCE))
                        .getCompany(nameCompany).getPeriods().get(0).getEndDate());
    }

    @Test
    void setEndDate() {
        //EXPERIENCE
        String newEndDate = "20.11.2023";
        expectedCompany.getPeriods().get(0).setEndDate(newEndDate);
        ((CompanySection) resume.getSection(SectionType.EXPERIENCE))
                .getCompany(nameCompany).getPeriods().get(0).setEndDate(newEndDate);

        assertEquals(expectedCompany.getPeriods().get(0).getEndDate(),
                ((CompanySection) resume.getSection(SectionType.EXPERIENCE))
                        .getCompany(nameCompany).getPeriods().get(0).getEndDate());
    }

    @Test
    void getDescription() {
        //EXPERIENCE
        assertEquals(expectedCompany.getPeriods().get(0).getDescription(),
                ((CompanySection) resume.getSection(SectionType.EXPERIENCE))
                        .getCompany(nameCompany).getPeriods().get(0).getDescription());
    }

    @Test
    void setDescription() {
        //EXPERIENCE
        String newDescription = "New Description";
        expectedCompany.getPeriods().get(0).setDescription(newDescription);
        ((CompanySection) resume.getSection(SectionType.EXPERIENCE))
                .getCompany(nameCompany).getPeriods().get(0).setDescription(newDescription);

        assertEquals(expectedCompany.getPeriods().get(0).getDescription(),
                ((CompanySection) resume.getSection(SectionType.EXPERIENCE))
                        .getCompany(nameCompany).getPeriods().get(0).getDescription());
    }
}