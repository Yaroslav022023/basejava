package com.basejava.webapp.storage.serializer;

import com.basejava.webapp.model.*;
import com.basejava.webapp.util.KeyValueDataStreamReader;
import com.basejava.webapp.util.KeyValueDataStreamSaver;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class DataStreamSerializer implements StreamSerializerStrategy {
    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());

            writeWithException(resume.getContacts().entrySet(), dos, (contactEntry) -> {
                dos.writeUTF(contactEntry.getKey().name());
                dos.writeUTF(contactEntry.getValue());
            });

            writeWithException(resume.getSections().entrySet(), dos, (sectionEntry) -> {
                dos.writeUTF(sectionEntry.getKey().name());
                Section sectionValue = sectionEntry.getValue();

                switch (sectionValue) {
                    case TextSection ts -> dos.writeUTF(ts.getText());
                    case ListSection ls -> writeWithException(ls.getTexts(), dos, dos::writeUTF);
                    case CompanySection cs -> writeWithException(cs.getCompanies(), dos, (companyEntry) -> {
                        dos.writeUTF(companyEntry.getName());
                        dos.writeUTF(companyEntry.getWebSite());

                        writeWithException(companyEntry.getPeriods(), dos, (periodEntry) -> {
                            dos.writeUTF(periodEntry.getTitle());
                            dos.writeUTF(periodEntry.getStartDate().toString());
                            dos.writeUTF(periodEntry.getEndDate().toString());
                            dos.writeUTF(Objects.toString(periodEntry.getDescription(), ""));
                        });
                    });
                    default -> throw new IllegalArgumentException("Unsupported Section class: " +
                            sectionValue.getClass().getSimpleName());
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        Resume resume;
        try (DataInputStream dis = new DataInputStream(is)) {
            resume = new Resume(dis.readUTF(), dis.readUTF());

            readWithException(dis, () -> resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));

            readWithException(dis, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                switch (sectionType) {
                    case OBJECTIVE, PERSONAL -> resume.addSection(sectionType, new TextSection(dis.readUTF()));
                    case ACHIEVEMENT, QUALIFICATION -> {
                        List<String> texts = new ArrayList<>();
                        readWithException(dis, () -> texts.add(dis.readUTF()));
                        resume.addSection(sectionType, new ListSection(texts));
                    }
                    case EXPERIENCE, EDUCATION -> {
                        CompanySection companySection = new CompanySection();
                        readWithException(dis, () -> {
                            Company company = new Company();
                            company.setName(dis.readUTF());
                            company.setWebSite(dis.readUTF());

                            readWithException(dis, () -> {
                                String title = dis.readUTF();
                                LocalDate startDate = LocalDate.parse(dis.readUTF());
                                LocalDate endDate = LocalDate.parse(dis.readUTF());
                                String description = dis.readUTF();

                                company.addPeriod(new Company.Period(title, startDate, endDate,
                                        description.equals("") ? null : description));
                            });
                            companySection.addCompany(company);
                        });
                        resume.addSection(sectionType, companySection);
                    }
                    default -> throw new IllegalArgumentException("Unsupported SectionType: " + sectionType);
                }
            });
        }
        return resume;
    }

    private <T> void writeWithException(Collection<T> collection, DataOutputStream dos,
                                        KeyValueDataStreamSaver<T> kvdss) throws IOException {
        dos.writeInt(collection.size());
        for (T elem : collection) {
            kvdss.saveToDataStream(elem);
        }
    }

    private void readWithException(DataInputStream dis, KeyValueDataStreamReader kvdsr) throws IOException {
        int amount = dis.readInt();
        for (int i = 0; i < amount; i++) {
            kvdsr.readFromDataStream();
        }
    }
}