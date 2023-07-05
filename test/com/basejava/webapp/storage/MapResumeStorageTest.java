package com.basejava.webapp.storage;

import com.basejava.webapp.exceptions.ExistStorageException;
import com.basejava.webapp.exceptions.NotExistStorageException;
import com.basejava.webapp.model.Resume;

import static org.junit.jupiter.api.Assertions.*;

public class MapResumeStorageTest extends AbstractStorageTest{
    protected MapResumeStorageTest() {
        super(new MapResumeStorage());
    }

    @Override
    void delete() {
        storage.delete(FULL_NAME_1);

        assertSize(2);
        assertThrows(NotExistStorageException.class, () -> storage.get(FULL_NAME_1));
    }

    @Override
    void update(){
        assertSame(r1, storage.get(r1.getFullName()));
        storage.update(r1);

        assertThrows(NotExistStorageException.class, () -> storage.update(r4));
    }

    @Override
    void saveExistResume() {
        assertThrows(ExistStorageException.class, () -> storage.save(r1));
    }

    @Override
    void getNotExistResume() {
        assertThrows(NotExistStorageException.class, () -> storage.get(FULL_NAME_4));
    }

    @Override
    protected void assertGet(Resume resume) {
        assertEquals(resume, storage.get(resume.getFullName()));
    }
}