package com.basejava.webapp.storage;

import com.basejava.webapp.exceptions.StorageException;
import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage{
    protected static final int CAPACITY = 10000;
    protected final Resume[] storage = new Resume[CAPACITY];

    protected abstract int getIndex(String uuid);
    protected abstract void insertResume(Resume resume, int index);
    protected abstract void removeResume(int index);

    public final Resume[] getAllFromIndividualStorage() {
        return Arrays.copyOf(storage, countResumes);
    }

    protected boolean saveIndividualStorage(Resume resume) {
        int index = getIndex(resume.getUuid());

        if (index >= 0) {
            return false;
        } else if (countResumes == CAPACITY) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        insertResume(resume, index);
        countResumes++;
        return true;
    }

    public final boolean deleteFromIndividualStorage(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            removeResume(index);
            return true;
        }
        return false;
    }

    public final Resume getFromIndividualStorage(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            return storage[index];
        }
        return null;
    }

    public final void clearFromIndividualStorage() {
        Arrays.fill(storage, 0, countResumes, null);
    }

    public final boolean updateFromIndividualStorage(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            storage[index] = resume;
            return true;
        }
        return false;
    }
}