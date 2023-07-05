package com.basejava.webapp.storage;

import com.basejava.webapp.exceptions.*;
import com.basejava.webapp.model.Resume;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractStorageTest {
    protected final Storage storage;
    private static final String UUID_1 = "UUID_1";
    private static final String UUID_2 = "UUID_2";
    private static final String UUID_3 = "UUID_3";
    private static final String UUID_4 = "UUID_4";
    protected static final String FULL_NAME_1 = "Bob";
    protected static final String FULL_NAME_2 = "Bob";
    protected static final String FULL_NAME_3 = "Bob";
    protected static final String FULL_NAME_4 = "Bob";
    protected final Resume r1 = new Resume(UUID_1, FULL_NAME_1);
    protected final Resume r2 = new Resume(UUID_2, FULL_NAME_2);
    protected final Resume r3 = new Resume(UUID_3, FULL_NAME_3);
    protected final Resume r4 = new Resume(UUID_4, FULL_NAME_4);

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
    void update(){
        assertSame(r1, storage.get(r1.getUuid()));
        storage.update(r1);

        assertThrows(NotExistStorageException.class, () -> storage.update(r4));
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