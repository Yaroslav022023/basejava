package com.basejava.webapp.storage;

import com.basejava.webapp.ResumeTestData;
import com.basejava.webapp.exceptions.ExistStorageException;
import com.basejava.webapp.exceptions.NotExistStorageException;
import com.basejava.webapp.model.ListSection;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.model.SectionType;
import com.basejava.webapp.model.TextSection;
import com.basejava.webapp.util.Config;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractStorageTest {
    protected static final String STORAGE_DIR = String.valueOf(Config.getInstance().getStorageDir());
    protected static final File STORAGE_DIR_FILE = Config.getInstance().getStorageDir();
    protected final Storage storage;
    private static final String UUID_1 = UUID.randomUUID().toString();
    private static final String UUID_2 = UUID.randomUUID().toString();
    private static final String UUID_3 = UUID.randomUUID().toString();
    private static final String UUID_4 = UUID.randomUUID().toString();
    protected static final String FULL_NAME_1 = "FULL_NAME_1";
    protected static final String FULL_NAME_2 = "FULL_NAME_2";
    protected static final String FULL_NAME_3 = "FULL_NAME_3";
    protected static final String FULL_NAME_4 = "FULL_NAME_4";
    private final Resume r1 = ResumeTestData.createResume(UUID_1, FULL_NAME_1);
    private final Resume r2 = ResumeTestData.createResume(UUID_2, FULL_NAME_2);
    private final Resume r3 = ResumeTestData.createResume(UUID_3, FULL_NAME_3);
    private final Resume r4 = ResumeTestData.createResume(UUID_4, FULL_NAME_4);

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    void setUp() {
        storage.save(r1);
        storage.save(r2);
        storage.save(r3);
    }

    @AfterEach
    void afterEach() {
        storage.clear();
    }

    @Test
    void getAllSorted() {
        ArrayList<Resume> expected = new ArrayList<>();
        expected.add(r1);
        expected.add(r2);
        expected.add(r3);
        expected.sort(combinedComparator());

        assertIterableEquals(expected, storage.getAllSorted());
    }

    @Test
    void getAllSortedWithoutContactsAndSections() {
        // Creating resume without contacts and sections.
        // r1, r2 - with contacts and sections, r3 - without.
        final String UUID_5 = UUID.randomUUID().toString();
        final String FULL_NAME_5 = "FULL_NAME_5";
        final Resume r3 = ResumeTestData.createResumeWithoutContactAndSection(UUID_5, FULL_NAME_5);

        ArrayList<Resume> expected = new ArrayList<>();
        expected.add(r1);
        expected.add(r2);
        expected.add(r3);
        expected.sort(combinedComparator());

        storage.delete(UUID_3);
        storage.save(r3);

        assertIterableEquals(expected, storage.getAllSorted());
    }

    @Test
    void size() {
        assertSize(3);
    }

    @Test
    void save() {
        storage.save(r4);

        assertGet(r4);
        assertSize(4);
    }

    @Test
    void delete() {
        storage.delete(UUID_1);

        assertSize(2);
        assertThrows(NotExistStorageException.class, () -> storage.get(UUID_1));
    }

    @Test
    void get() {
        assertGet(r1);
        assertGet(r2);
        assertGet(r3);
    }

    @Test
    void clear() {
        storage.clear();


        assertEquals(0, storage.getSize());
        assertSize(0);
    }

    @Test
    void update() {
        assertEquals(r1, storage.get(r1.getUuid()));
        storage.update(r1);

        assertThrows(NotExistStorageException.class, () -> storage.update(r4));
    }

    @Test
    void updateObjectiveSection() {
        SectionType objective = SectionType.OBJECTIVE;
        if (r1.getSection(objective) != null) {
            String updatedText = "text2 for OBJECTIVE, text2, text2, text2";
            ((TextSection) r1.getSection(objective)).setText(updatedText);
            storage.update(r1);
            assertEquals(r1, storage.get(r1.getUuid()));
        }
    }

    @Test
    void updatePersonalSection() {
        SectionType personal = SectionType.PERSONAL;
        if (r1.getSection(personal) != null) {
            String updatedText = "text2 for PERSONAL, text2, text2, text2";
            ((TextSection) r1.getSection(personal)).setText(updatedText);
            storage.update(r1);
            assertEquals(r1, storage.get(r1.getUuid()));
        }
    }

    @Test
    void updateAchievementSection() {
        SectionType achievement = SectionType.ACHIEVEMENT;
        if (r1.getSection(achievement) != null) {
            String updatedText = "text2 for ACHIEVEMENT, text2";
            ((ListSection) r1.getSection(achievement)).clear();
            ((ListSection) r1.getSection(achievement)).addText(updatedText);
            storage.update(r1);
            assertEquals(r1, storage.get(r1.getUuid()));
        }
    }

    @Test
    void updateQualificationSection() {
        SectionType qualification = SectionType.QUALIFICATION;
        if (r1.getSection(qualification) != null) {
            String updatedText = "text2 for QUALIFICATION, text2";
            ((ListSection) r1.getSection(qualification)).clear();
            ((ListSection) r1.getSection(qualification)).addText(updatedText);
            storage.update(r1);
            assertEquals(r1, storage.get(r1.getUuid()));
        }
    }

    @Test
    void saveExistResume() {
        assertThrows(ExistStorageException.class, () -> storage.save(r1));
    }

    @Test
    void getNotExistResume() {
        assertThrows(NotExistStorageException.class, () -> storage.get(UUID_4));
    }

    protected void assertSize(int size) {
        assertEquals(size, storage.getSize());
    }

    protected void assertGet(Resume resume) {
        assertEquals(resume, storage.get(resume.getUuid()));
    }

    private Comparator<Resume> combinedComparator() {
        Comparator<Resume> fullNameComparator = Comparator.comparing(Resume::getFullName);
        Comparator<Resume> uuidComparator = Comparator.comparing(Resume::getUuid);

        return fullNameComparator.thenComparing(uuidComparator);
    }
}