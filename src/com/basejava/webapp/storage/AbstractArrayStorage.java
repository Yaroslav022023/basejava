package com.basejava.webapp.storage;

import com.basejava.webapp.exceptions.StorageException;
import com.basejava.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage{
    protected static final int CAPACITY = 10000;
    protected final Resume[] storage = new Resume[CAPACITY];
    protected int countResumes;

    @Override
    protected final List<Resume> doGetAllSorted() {
        ArrayList<Resume> storageForTransfer = new ArrayList<>();
        for (Resume resume : storage) {
            if (resume != null) {
                storageForTransfer.add(resume);
            }
        }
        return storageForTransfer;
    }

    @Override
    public final int getSize() {
        return countResumes;
    }

    @Override
    protected final void doSave(Resume resume, Object index) {
        if (countResumes == CAPACITY) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        insertResume(resume, (int) index);
        countResumes++;
    }

    @Override
    protected final void doDelete(Object searchedKey) {
        removeResume((int) searchedKey);
        countResumes--;
    }

    @Override
    protected final Resume doGet(Object searchedKey) {
        return storage[(int) searchedKey];
    }

    @Override
    protected final void doClear() {
        Arrays.fill(storage, 0, countResumes, null);
        countResumes = 0;
    }

    @Override
    protected final void doUpdate(Object searchedKey, Resume resume) {
        storage[(int) searchedKey] = resume;
    }

    @Override
    protected final boolean isExisting(Object searchKey) {
        return (int) searchKey >= 0;
    }

    protected abstract Object getSearchKey(String uuid);
    protected abstract void insertResume(Resume resume, int index);
    protected abstract void removeResume(int index);
}