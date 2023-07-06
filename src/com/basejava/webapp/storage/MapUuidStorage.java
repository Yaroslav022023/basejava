package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUuidStorage extends AbstractStorage {
    protected final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected final List<Resume> doGetAllSorted() {
        return new ArrayList<>(this.storage.values());
    }

    @Override
    public final int getSize() {
        return storage.size();
    }

    @Override
    protected final void doSave(Resume resume, Object index) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected void doDelete(Object searchedKey) {
        storage.remove((String) searchedKey);
    }

    @Override
    protected Resume doGet(Object searchedKey) {
        return storage.get((String) searchedKey);
    }

    @Override
    protected final void doClear() {
        storage.clear();
    }

    @Override
    protected void doUpdate(Object searchedKey, Resume resume) {
        storage.replace((String) searchedKey, resume);
    }

    @Override
    protected boolean isExisting(Object searchKey) {
        return storage.containsKey((String) searchKey);
    }
}