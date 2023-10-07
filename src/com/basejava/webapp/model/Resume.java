package com.basejava.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serial;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String uuid;
    private String fullName;
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

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public final Map<ContactType, String> getContacts() {
        return contacts;
    }

    public final Map<SectionType, Section> getSections() {
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

    public final void addContact(ContactType type, String data) {
        contacts.put(type, data);
    }

    public final void addSection(SectionType type, Section section) {
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
        return Objects.equals(getUuid(), resume.getUuid()) &&
                Objects.equals(getFullName(), resume.getFullName()) &&
                Objects.equals(contacts, resume.contacts) &&
                Objects.equals(sections, resume.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), getFullName(), contacts, sections);
    }

    @Override
    public String toString() {
        return uuid + '(' + fullName + ')';
    }
}