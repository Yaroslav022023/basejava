package com.basejava.webapp.model;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Resume {
    private final String uuid;
    private final String fullName;
    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);

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

    public final Map<SectionType, Section> getAllSections() {
        return sections;
    }

    public final String getContact(ContactType type) {
        return contacts.get(type);
    }

    public final Section getSection(SectionType type) {
        return sections.get(type);
    }

    public final void changeContacts(ContactType type, String data) {
        contacts.replace(type, data);
    }

    public final void removeContacts(ContactType type) {
        contacts.remove(type);
    }

    public final void addContacts(ContactType type, String data) {
        contacts.put(type, data);
    }

    public final void addSections(SectionType type, Section section) {
        sections.put(type, section);
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