package com.basejava.webapp.storage;

import com.basejava.webapp.exceptions.ExistStorageException;
import com.basejava.webapp.exceptions.NotExistStorageException;
import com.basejava.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {
    protected int countResumes;

    @Override
    public final int getSize() {
        return countResumes;
    }

    protected abstract void doSave(Resume resume, Object index);
    protected abstract void doDelete(Object searchedKey);
    protected abstract Resume doGet(Object searchedKey);
    protected abstract void doClear();
    protected abstract void doUpdate(Resume resume, Object searchedKey);
    protected abstract Object getSearchKey(String uuid);
    protected abstract boolean isExisting (Object searchKey);

    @Override
    public final void save(Resume resume) {
        Object searchedKey = findNotExistingSearchKey(resume.getUuid());
        doSave(resume, searchedKey);
        countResumes++;
        System.out.println("The resume '" + resume.getUuid() + "' was successfully added.");
    }

    @Override
    public final void delete(String uuid) {
        Object searchedKey = findExistingSearchKey(uuid);
        doDelete(searchedKey);
        countResumes--;
        System.out.println("The resume '" + uuid + "' was successfully deleted.");
    }

    @Override
    public final Resume get(String uuid) {
        return doGet(findExistingSearchKey(uuid));
    }

    @Override
    public final void clear() {
        doClear();
        countResumes = 0;
        System.out.println("The storage has been cleared");
    }

    @Override
    public final void update(Resume resume) {
        Object searchedKey = findExistingSearchKey(resume.getUuid());
        doUpdate(resume, searchedKey);
        System.out.println("The resume '" + resume.getUuid() + "' was successfully updated.");
    }

    private Object findNotExistingSearchKey(String uuid) {
        Object searchedKey = getSearchKey(uuid);
        if (!isExisting(searchedKey)) {
            return searchedKey;
        }
        throw new ExistStorageException(uuid);
    }

    private Object findExistingSearchKey(String uuid) {
        Object searchedKey = getSearchKey(uuid);
        if (isExisting(searchedKey)) {
            return searchedKey;
        }
        throw new NotExistStorageException(uuid);
    }
}