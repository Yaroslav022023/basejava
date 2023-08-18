package com.basejava.webapp.storage.serializer;

import com.basejava.webapp.model.*;
import com.basejava.webapp.util.KeyValueDataStreamSaver;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataStreamSerializer implements StreamSerializerStrategy {
    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());

            writeWithException(resume.getAllContacts().entrySet(), dos,
                    (entries) -> {
                        for (Object collection : entries) {
                            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) collection;
                            dos.writeUTF(entry.getKey().toString());
                            dos.writeUTF(entry.getValue().toString());
                        }
            });

            writeWithException(resume.getAllSections().entrySet(), dos,
                    (entries) -> {
                for (Object collection : entries) {
                    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) collection;
                    dos.writeUTF(entry.getKey().toString());
                    Section sectionValue = (Section) entry.getValue();

                    switch (sectionValue) {
                        case TextSection ts -> dos.writeUTF(ts.getText());
                        case ListSection ls -> {
                            dos.writeInt(ls.getTexts().size());
                            for (String string : ls.getTexts()) {
                                dos.writeUTF(string);
                            }
                        }
                        case CompanySection cs -> {
                            dos.writeInt(cs.getCompanies().size());
                            for (Company company : cs.getCompanies()) {
                                dos.writeUTF(company.getName());
                                dos.writeUTF(company.getWebSite());

                                dos.writeInt(company.getPeriods().size());
                                for (Company.Period period : company.getPeriods()) {
                                    dos.writeUTF(period.getTitle());
                                    dos.writeUTF(period.getStartDate().toString());
                                    dos.writeUTF(period.getEndDate().toString());
                                    dos.writeUTF(Objects.toString(period.getDescription(), ""));
                                }
                            }
                        }
                        default -> throw new IllegalArgumentException("Unsupported Section class: " +
                                sectionValue.getClass().getSimpleName());
                    }
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        Resume resume;
        try (DataInputStream dis = new DataInputStream(is)) {
            resume = new Resume(dis.readUTF(), dis.readUTF());

            int amountContacts = dis.readInt();
            for (int i = 0; i < amountContacts; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }

            int amountSections = dis.readInt();
            for (int i = 0; i < amountSections; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                switch (sectionType) {
                    case OBJECTIVE, PERSONAL -> createTextSection(resume, sectionType, dis);
                    case ACHIEVEMENT, QUALIFICATION -> createListSection(resume, sectionType, dis);
                    case EXPERIENCE, EDUCATION -> createCompanySection(resume, sectionType, dis);
                    default -> throw new IllegalArgumentException("Unsupported SectionType: "
                            + sectionType);
                }
            }
        }
        return resume;
    }

    private void createTextSection(Resume resume, SectionType sectionType,
                                   DataInputStream dis) throws IOException {
        resume.addSection(sectionType, new TextSection(dis.readUTF()));
    }

    private void createListSection(Resume resume, SectionType sectionType,
                                   DataInputStream dis) throws IOException {
        int amountTexts = dis.readInt();
        List<String> texts = new ArrayList<>();
        for (int i = 0; i < amountTexts; i++) {
            texts.add(dis.readUTF());
        }
        resume.addSection(sectionType, new ListSection(texts));
    }

    private void createCompanySection(Resume resume, SectionType sectionType,
                                      DataInputStream dis) throws IOException {
        CompanySection companySection = new CompanySection();
        int amountCompanies = dis.readInt();
        for (int i = 0; i < amountCompanies; i++) {
            Company company = new Company();
            company.setName(dis.readUTF());
            company.setWebSite(dis.readUTF());

            int amountPeriods = dis.readInt();
            for (int j = 0; j < amountPeriods; j++) {
                String title = dis.readUTF();
                LocalDate startDate = LocalDate.parse(dis.readUTF());
                LocalDate endDate = LocalDate.parse(dis.readUTF());
                String description = dis.readUTF();

                company.addPeriod(new Company.Period(title, startDate, endDate,
                        description.equals("") ? null : description));
            }
            companySection.addCompany(company);
        }
        resume.addSection(sectionType, companySection);
    }

    private <K,V> void writeWithException(Collection<?> collection, DataOutputStream dos,
                                          KeyValueDataStreamSaver<K, V> kvdss) throws IOException {
        dos.writeInt(collection.size());
        kvdss.saveToDataStream(collection);
    }
}