package com.basejava.webapp.storage;

import com.basejava.webapp.exceptions.*;
import com.basejava.webapp.model.Resume;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractStorageTest {
    protected final Storage storage;
    private static final String UUID_1 = "UUID_1";
    private static final String UUID_2 = "UUID_2";
    private static final String UUID_3 = "UUID_3";
    private static final String UUID_4 = "UUID_4";
    protected final Resume r1 = new Resume(UUID_1);
    protected final Resume r2 = new Resume(UUID_2);
    protected final Resume r3 = new Resume(UUID_3);
    protected final Resume r4 = new Resume(UUID_4);

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
    final void getAll() {
        Resume[] expected = new Resume[]{r1,r2,r3};

        Assertions.assertArrayEquals(expected, storage.getAll());
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
        assertThrows(NotExistStorageException.class, () -> {
           storage.get(UUID_1);
        });
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

        Resume[] emptyStorage = new Resume[0];

        assertArrayEquals(emptyStorage, storage.getAll());
        assertSize(0);
    }

    @Test
    void update(){
        assertSame(r1, storage.get(r1.getUuid()));
        storage.update(r1);

        assertThrows(NotExistStorageException.class, () -> {
           storage.update(r4);
        });
    }

    @Test
    void saveExistResume() {
        assertThrows(ExistStorageException.class, () -> {
            storage.save(r1);
        });
    }

    @Test
    void getNotExistResume() {
        assertThrows(NotExistStorageException.class, () -> {
            storage.get(UUID_4);
        });
    }

    private void assertSize(int size) {
        assertEquals(size, storage.getSize());
    }

    private void assertGet(Resume resume) {
        assertEquals(resume, storage.get(resume.getUuid()));
    }
}