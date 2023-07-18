package com.basejava.webapp.model;

public enum Sections {
    OBJECTIVE("Position"),
    PERSONAL("Personal qualities"),
    ACHIEVEMENT("Achievement"),
    QUALIFICATION("Qualification"),
    EXPERIENCE("Experience"),
    EDUCATION("Education");

    private final String section;

    Sections(String section) {
        this.section = section;
    }

    public final String getSection() {
        return section;
    }
}