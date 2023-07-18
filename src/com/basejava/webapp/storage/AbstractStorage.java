package com.basejava.webapp.storage;

import com.basejava.webapp.exceptions.ExistStorageException;
import com.basejava.webapp.exceptions.NotExistStorageException;
import com.basejava.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Comparator.comparing;

public abstract class AbstractStorage<SK> implements Storage {
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());
    protected static final Comparator<Resume> COMPARATOR =
            comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    @Override
    public final List<Resume> getAllSorted() {
        LOG.info("getAllSorted");

        List<Resume> storageSorted = new ArrayList<>(doCopyAll());
        storageSorted.sort(COMPARATOR);
        return storageSorted;
    }

    @Override
    public final void save(Resume resume) {
        LOG.info("saved: " + resume);

        SK searchedKey = checkNotExistingSearchKey(resume.getUuid());
        doSave(resume, searchedKey);
        System.out.println("The resume '" + resume.getUuid() + "' was successfully added.");
    }

    @Override
    public final void delete(String uuid) {
        LOG.info("deleted: " + uuid);

        SK searchedKey = checkExistingSearchKey(uuid);
        doDelete(searchedKey);
        System.out.println("The resume '" + uuid + "' was successfully deleted.");
    }

    @Override
    public final Resume get(String uuid) {
        LOG.info("got: " + uuid);

        return doGet(checkExistingSearchKey(uuid));
    }

    @Override
    public final void clear() {
        LOG.info("cleared");

        doClear();
        System.out.println("The storage has been cleared");
    }

    @Override
    public final void update(Resume resume) {
        LOG.info("updated: " + resume);

        SK searchedKey = checkExistingSearchKey(resume.getUuid());
        doUpdate(searchedKey, resume);
        System.out.println("The resume '" + resume.getUuid() + "' was successfully updated.");
    }

    private SK checkNotExistingSearchKey(String uuid) {
        SK searchedKey = getSearchKey(uuid);
        if (!isExisting(searchedKey)) {
            LOG.info("checkNotExistingSearchKey - passed: " + uuid);
            return searchedKey;
        }
        LOG.log(Level.WARNING, "Resume '" + uuid + "' already exist");
        throw new ExistStorageException(uuid);
    }

    private SK checkExistingSearchKey(String uuid) {
        SK searchedKey = getSearchKey(uuid);
        if (isExisting(searchedKey)) {
            LOG.info("checkExistingSearchKey - passed: " + uuid);
            return searchedKey;
        }
        LOG.log(Level.WARNING, "Resume '" + uuid + "' not exist");
        throw new NotExistStorageException(uuid);
    }

    protected abstract SK getSearchKey(String uuid);
    protected abstract List<Resume> doCopyAll();
    protected abstract void doSave(Resume resume, SK searchedKey);
    protected abstract void doDelete(SK searchedKey);
    protected abstract Resume doGet(SK searchedKey);
    protected abstract void doClear();
    protected abstract void doUpdate(SK searchedKey, Resume resume);
    protected abstract boolean isExisting (SK searchKey);
}