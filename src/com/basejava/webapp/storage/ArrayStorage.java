package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected final Object getSearchKey(String uuid) {
        for (int i = 0; i < countResumes; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected final void insertResume(Resume resume, int index) {
        storage[countResumes] = resume;
    }

    @Override
    protected final void removeResume(int index) {
        storage[index] = storage[countResumes - 1];
        storage[countResumes - 1] = null;
    }
}