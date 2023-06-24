package com.basejava.webapp.storage;

import com.basejava.webapp.exceptions.ExistStorageException;
import com.basejava.webapp.exceptions.NotExistStorageException;
import com.basejava.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {
    protected int countResumes;

    public final int getSize() {
        return countResumes;
    }

    protected abstract boolean saveIndividualStorage(Resume resume);
    protected abstract boolean deleteFromIndividualStorage(String uuid);
    protected abstract Resume getFromIndividualStorage(String uuid);
    protected abstract void clearFromIndividualStorage();
    protected abstract boolean updateFromIndividualStorage(Resume resume);
    protected abstract Resume[] getAllFromIndividualStorage();

    public final Resume[] getAll() {
        return getAllFromIndividualStorage();
    }

    public final void save(Resume resume) {
        if (!saveIndividualStorage(resume)) {
            throw new ExistStorageException(resume.getUuid());
        }
        System.out.println("The resume '" + resume.getUuid() + "' was successfully added.");
    }

    public final void delete(String uuid) {
        if (!deleteFromIndividualStorage(uuid)) {
            throw new NotExistStorageException(uuid);
        }
        countResumes--;
        System.out.println("The resume '" + uuid + "' was successfully deleted.");
    }

    public final Resume get(String uuid) {
        Resume currentResume = getFromIndividualStorage(uuid);

        if (currentResume == null) {
            throw new NotExistStorageException(uuid);
        }
        return currentResume;
    }


    public final void clear() {
        clearFromIndividualStorage();
        countResumes = 0;
        System.out.println("The storage has been cleared");
    }


    public final void update(Resume resume) {
        if (!updateFromIndividualStorage(resume)) {
            throw new NotExistStorageException(resume.getUuid());
        }
        System.out.println("The resume '" + resume.getUuid() + "' was successfully updated.");
    }
}