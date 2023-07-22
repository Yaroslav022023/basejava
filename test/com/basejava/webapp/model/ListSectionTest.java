package com.basejava.webapp.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListSectionTest extends AbstractResumeTest{
    private final String TEXT_1 = "text 1";
    private final String TEXT_2 = "text 2";
    private final String TEXT_3 = "text 3";
    private final ListSection expected = new ListSection(TEXT_1, TEXT_2, TEXT_3);

    @Test
    void getTexts() {
        //ACHIEVEMENT
        assertEquals(expected.getTexts(),
                ((ListSection) resume.getSection(SectionType.ACHIEVEMENT)).getTexts());
    }

    @Test
    void addText() {
        //ACHIEVEMENT
        assertSize(3, SectionType.ACHIEVEMENT);
        String newText = "New Text 5";
        expected.addText(newText);
        ((ListSection) resume.getSection(SectionType.ACHIEVEMENT)).addText(newText);

        assertEquals(expected.getTexts(),
                ((ListSection) resume.getSection(SectionType.ACHIEVEMENT)).getTexts());

        assertSize(4, SectionType.ACHIEVEMENT);
    }

    @Test
    void removeText() {
        //ACHIEVEMENT
        assertSize(3, SectionType.ACHIEVEMENT);
        ((ListSection) resume.getSection(SectionType.ACHIEVEMENT)).removeText(TEXT_1);
        assertSize(2, SectionType.ACHIEVEMENT);
    }

    @Test
    void clear() {
        //ACHIEVEMENT
        assertSize(3, SectionType.ACHIEVEMENT);
        ((ListSection) resume.getSection(SectionType.ACHIEVEMENT)).clear();
        assertSize(0, SectionType.ACHIEVEMENT);
    }

    protected void assertSize(int size, SectionType sectionType) {
        assertEquals(size, ((ListSection) resume.getSection(sectionType)).getTexts().size());
    }
}