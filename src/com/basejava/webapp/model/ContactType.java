package com.basejava.webapp.model;

public enum ContactType {
    PHONE("Tel.: "),
    SKYPE("Skype: "),
    EMAIL("Email: "),
    LINKEDIN("Profile LinkedIn: "),
    GITHUB("Profile GitHub: "),
    STACKOVERFLOW("Profile Stackoverflow: "),
    URL_HOMEPAGE("Home page");

    private final String contact;

    ContactType(String contact) {
        this.contact = contact;
    }

    public final String getContact() {
        return contact;
    }
}