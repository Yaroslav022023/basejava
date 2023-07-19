package com.basejava.webapp.model;

public enum SectionType {
    OBJECTIVE("Position"),
    PERSONAL("Personal qualities"),
    ACHIEVEMENT("Achievement"),
    QUALIFICATION("Qualification"),
    EXPERIENCE("Experience"),
    EDUCATION("Education");

    private final String section;

    SectionType(String section) {
        this.section = section;
    }

    public final String getSection() {
        return section;
    }
}