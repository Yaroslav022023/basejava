package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Map;
import java.util.Objects;

public class MapResumeStorage extends MapUuidStorage{

    @Override
    protected final Object getSearchKey(String uuid) {
        for (Map.Entry<String, Resume> entry : storage.entrySet()) {
            if (entry.getKey().equals(uuid)) {
                return entry;
            }
        }
        return null;
    }

    @Override
    protected final void doDelete(Object searchedKey) {
        storage.entrySet().removeIf(searchedKey::equals);
    }

    @Override
    protected final Resume doGet(Object searchedKey) {
        return Objects.requireNonNull(iterator(searchedKey)).getValue();
    }

    @Override
    protected void doUpdate(Object searchedKey, Resume resume) {
        Objects.requireNonNull(iterator(searchedKey)).setValue(resume);
    }

    @Override
    protected final boolean isExisting(Object searchKey) {
        return searchKey != null;
    }

    private Map.Entry<String, Resume> iterator(Object searchedKey) {
        for (Map.Entry<String, Resume> entry : storage.entrySet()) {
            if (searchedKey.equals(entry)) {
                return entry;
            }
        }
        return null;
    }
}