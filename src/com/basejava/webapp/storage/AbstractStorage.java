package com.basejava.webapp.storage;

import com.basejava.webapp.exceptions.ExistStorageException;
import com.basejava.webapp.exceptions.NotExistStorageException;
import com.basejava.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage implements Storage {

    @Override
    public final List<Resume> getAllSorted() {
        List<Resume> storageSorted = new ArrayList<>(doGetAllSorted());
        storageSorted.sort(combinedComparator());
        return storageSorted;
    }

    @Override
    public final void save(Resume resume) {
        Object searchedKey = findNotExistingSearchKey(resume.getUuid());
        doSave(resume, searchedKey);
        System.out.println("The resume '" + resume.getUuid() + "' was successfully added.");
    }

    @Override
    public final void delete(String uuid) {
        Object searchedKey = findExistingSearchKey(uuid);
        doDelete(searchedKey);
        System.out.println("The resume '" + uuid + "' was successfully deleted.");
    }

    @Override
    public final Resume get(String uuid) {
        return doGet(findExistingSearchKey(uuid));
    }

    @Override
    public final void clear() {
        doClear();
        System.out.println("The storage has been cleared");
    }

    @Override
    public final void update(Resume resume) {
        Object searchedKey = findExistingSearchKey(resume.getUuid());
        doUpdate(searchedKey, resume);
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

    protected Comparator<Resume> combinedComparator() {
        Comparator<Resume> fullNameComparator = Comparator.comparing(Resume::getFullName);
        Comparator<Resume> uuidComparator = Comparator.comparing(Resume::getUuid);

        return fullNameComparator.thenComparing(uuidComparator);
    }

    protected abstract List<Resume> doGetAllSorted();
    protected abstract void doSave(Resume resume, Object index);
    protected abstract void doDelete(Object searchedKey);
    protected abstract Resume doGet(Object searchedKey);
    protected abstract void doClear();
    protected abstract void doUpdate(Object searchedKey, Resume resume);
    protected abstract Object getSearchKey(String uuid);
    protected abstract boolean isExisting (Object searchKey);
}