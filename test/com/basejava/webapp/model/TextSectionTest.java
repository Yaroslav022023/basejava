package com.basejava.webapp.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextSectionTest extends AbstractResumeTest{
    private final String TEXT = "text text text text text";
    private final TextSection expected = new TextSection(TEXT);

    @Test
    void getText() {
        //OBJECTIVE
        assertEquals(TEXT, ((TextSection) resume.getSection(SectionType.OBJECTIVE)).getText());
    }

    @Test
    void setText() {
        //OBJECTIVE
        String newText = "New text";
        expected.setText(newText);
        ((TextSection) resume.getSection(SectionType.OBJECTIVE)).setText(newText);

        assertEquals(expected.getText(), ((TextSection) resume.getSection(SectionType.OBJECTIVE)).getText());
    }
}