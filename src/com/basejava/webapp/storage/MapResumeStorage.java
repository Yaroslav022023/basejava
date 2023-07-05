package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage {
    private final Map<String, Resume> storage = new HashMap<>();

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
    protected final void doDelete(Object searchedKey) {
        storage.remove((String) searchedKey);
    }

    @Override
    protected final Resume doGet(Object searchedKey) {
        return storage.get((String) searchedKey);
    }

    @Override
    protected final void doClear() {
        storage.clear();
    }

    @Override
    protected final void doUpdate(Object searchedKey, Resume resume) {
        storage.replace((String) searchedKey, resume);
    }

    @Override
    protected final Object getSearchKey(String fullName) {
        for (Map.Entry<String, Resume> entry : storage.entrySet()) {
            if (entry.getValue().getUuid().equals(fullName)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    protected final boolean isExisting(Object searchKey) {
        return searchKey != null;
    }
}