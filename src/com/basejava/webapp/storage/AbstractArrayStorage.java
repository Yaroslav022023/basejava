package com.basejava.webapp.storage;

import com.basejava.webapp.exceptions.StorageException;
import com.basejava.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer>{
    protected static final int CAPACITY = 10000;
    protected final Resume[] storage = new Resume[CAPACITY];
    protected int countResumes;

    @Override
    protected final List<Resume> doCopyAll() {
        return Arrays.asList(Arrays.copyOfRange(storage, 0, countResumes));
    }

    @Override
    public final int getSize() {
        return countResumes;
    }

    @Override
    protected final void doSave(Resume resume, Integer index) {
        if (countResumes == CAPACITY) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        insertResume(resume, index);
        countResumes++;
    }

    @Override
    protected final void doDelete(Integer index) {
        removeResume(index);
        countResumes--;
    }

    @Override
    protected final Resume doGet(Integer index) {
        return storage[index];
    }

    @Override
    protected final void doClear() {
        Arrays.fill(storage, 0, countResumes, null);
        countResumes = 0;
    }

    @Override
    protected final void doUpdate(Integer index, Resume resume) {
        storage[index] = resume;
    }

    @Override
    protected final boolean isExisting(Integer index) {
        return index >= 0;
    }

    protected abstract void insertResume(Resume resume, int index);
    protected abstract void removeResume(int index);
}