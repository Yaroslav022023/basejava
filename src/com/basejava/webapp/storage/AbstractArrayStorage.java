package com.basejava.webapp.storage;

import com.basejava.webapp.exceptions.StorageException;
import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage{
    protected static final int CAPACITY = 10000;
    protected final Resume[] storage = new Resume[CAPACITY];

    protected abstract Object getSearchKey(String uuid);
    protected abstract void insertResume(Resume resume, int index);
    protected abstract void removeResume(int index);

    @Override
    public final Resume[] getAll() {
        return Arrays.copyOf(storage, countResumes);
    }

    @Override
    protected final void doSave(Resume resume, Object index) {
        if (countResumes == CAPACITY) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        insertResume(resume, (int) index);
    }

    @Override
    protected final void doDelete(Object searchedKey) {
        removeResume((int) searchedKey);
    }

    @Override
    protected final Resume doGet(Object searchedKey) {
        return storage[(int) searchedKey];
    }

    @Override
    protected final void doClear() {
        Arrays.fill(storage, 0, countResumes, null);
    }

    @Override
    protected final void doUpdate(Resume resume, Object searchedKey) {
        storage[(int) searchedKey] = resume;
    }

    @Override
    protected final boolean isExisting(Object searchKey) {
        return (int) searchKey >= 0;
    }
}