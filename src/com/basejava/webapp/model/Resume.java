package com.basejava.webapp.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Resume {
    private final String uuid;
    private final String fullName;
    private final Map<ContactType, String> contacts = new HashMap<>();
    private final Map<Sections, Section> sections = new HashMap<>();

    public Resume() {
        this(UUID.randomUUID().toString(), "");
    }

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "uuid must not be null");
        Objects.requireNonNull(fullName, "fullName must not be null");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public final String getUuid() {
        return uuid;
    }

    public final String getFullName() {
        return fullName;
    }

    public final Map<ContactType, String> getAllContacts() {
        return contacts;
    }

    public final Map<Sections, Section> getAllSections() {
        return sections;
    }

    public final String getContact(ContactType contactType) {
        return contacts.get(contactType);
    }

    public final Section getSection(Sections enumSection) {
        return sections.get(enumSection);
    }

    public final void changeContacts(ContactType contactType, String data) {
        contacts.replace(contactType, data);
    }

    public final void removeContacts(ContactType contactType) {
        contacts.remove(contactType);
    }

    public final void addContacts(ContactType enumContactType, String data) {
        contacts.put(enumContactType, data);
    }

    public final void addSections(Sections enumSections, Section section) {
        sections.put(enumSections, section);
    }

    public final void clearContacts() {
        contacts.clear();
    }

    public final void clearSections() {
        sections.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return getUuid().equals(resume.getUuid()) && getFullName().equals(resume.getFullName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), getFullName());
    }

    @Override
    public String toString() {
        return uuid + '(' + fullName + ')';
    }
}