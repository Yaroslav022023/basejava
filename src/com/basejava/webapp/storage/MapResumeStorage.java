package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.*;

public class MapResumeStorage extends AbstractStorage<Resume>{
    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected final Resume getSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected final List<Resume> doCopyAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public final int getSize() {
        return storage.size();
    }

    @Override
    protected final void doSave(Resume resume, Resume searchKey) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected final void doDelete(Resume searchKey) {
        storage.remove(searchKey.getUuid());
    }

    @Override
    protected final Resume doGet(Resume searchKey) {
        return storage.get(searchKey.getUuid());
    }

    @Override
    protected final void doClear() {
        storage.clear();
    }

    @Override
    protected final void doUpdate(Resume searchKey, Resume resume) {
        storage.replace(searchKey.getUuid(), resume);
    }

    @Override
    protected final boolean isExisting(Resume searchKey) {
        return searchKey != null;
    }
}