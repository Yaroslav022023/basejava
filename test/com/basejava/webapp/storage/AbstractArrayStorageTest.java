package com.basejava.webapp.storage;

import com.basejava.webapp.exceptions.*;
import com.basejava.webapp.model.Resume;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.fail;

public abstract class AbstractArrayStorageTest {
    private final Storage storage;

    protected AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    Resume r1 = new Resume("UUID_1");
    Resume r2 = new Resume("UUID_2");
    Resume r3 = new Resume("UUID_3");
    Resume r4 = new Resume("UUID_25");
    Resume r5 = new Resume();

    @BeforeEach
    void setUp() {
        storage.save(r1);
        storage.save(r2);
        storage.save(r3);
        storage.save(r4);
        storage.save(r5);
    }

    @AfterEach
    void afterEach() {
        storage.clear();
    }

    @Test
    final void getAll() {
        Assertions.assertArrayEquals(storage.getAll(), getAll_CompareWithIndividualStorage());
    }

    protected abstract Resume[] getAll_CompareWithIndividualStorage();

    @Test
    void getSize() {
        Assertions.assertEquals(5, storage.getSize());
    }

    @Test
    void save() {
        int currentSize = storage.getSize();
        Resume r1 = new Resume();
        storage.save(r1);

        Assertions.assertEquals(storage.get(r1.getUuid()), r1);
        Assertions.assertEquals(storage.getSize(), currentSize + 1);
    }

    @Test
    void delete() {
        int currentSize = storage.getSize();
        storage.delete(r2.getUuid());
        storage.delete(r4.getUuid());

        Assertions.assertEquals(storage.getSize(), currentSize - 2);
    }

    @Test
    void get() {
        Assertions.assertEquals(storage.get("UUID_1").toString(), "UUID_1");
        Assertions.assertEquals(storage.get("UUID_25").toString(), "UUID_25");
    }

    @Test
    void clear() {
        storage.clear();
        Assertions.assertEquals(0, storage.getSize());
    }

    @Test
    void update(){
        Resume r1 = new Resume("UUID_3");
        try {
            storage.update(r1);
        } catch (NotExistStorageException e) {
            fail("Not exist '" + r1.getUuid() + "' resume");
        }

    }

    @Test
    void saveExistResume() {
        Resume r1 = new Resume("UUID_1");
        Assertions.assertThrows(ExistStorageException.class, () -> {
            storage.save(r1);
        });
    }

    @Test
    void getNotExistResume() {
        Assertions.assertThrows(NotExistStorageException.class, () -> {
            storage.get("UUID_100");
        });
    }

    @Test
    void storageException() {
        int capacity = 100;
        Resume[] resumes = new Resume[capacity];
        try {
            for (int i = 0; i < resumes.length; i++) {
                resumes[i] = new Resume();
            }
        } catch (Exception e) {
            fail("Overload occurred ahead of time");
        }

        Assertions.assertThrows(StorageException.class, () -> {
            Resume r1 = new Resume();
            try {
                resumes[capacity] = r1;
            } catch (IndexOutOfBoundsException e) {
                throw new StorageException("Storage overflow", r1.getUuid());
            }
        });
    }
}