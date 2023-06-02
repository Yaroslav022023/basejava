package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected Resume[] getAllConcreteStorage() {
        return Arrays.copyOf(storage, countResumes);
    }

    @Override
    protected void saveConcreteStorage(Resume resume, int index) {
        storage[countResumes] = resume;
        countResumes++;
    }

    @Override
    protected void deleteFromConcreteStorage(int index) {
        storage[index] = storage[countResumes];
        storage[countResumes] = null;
    }

    @Override
    protected Resume getFromConcreteStorage(int index) {
        return storage[index];
    }

    @Override
    protected void clearConcreteStorage() {
        Arrays.fill(storage, 0, countResumes, null);
    }

    @Override
    protected void updateConcreteStorage(Resume resume, int index) {
        storage[index] = resume;
    }

    @Override
    protected int getIndex(String uuid) {
        for (int i = 0; i < countResumes; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }
}