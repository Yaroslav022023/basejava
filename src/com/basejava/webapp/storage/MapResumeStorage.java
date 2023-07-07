package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.*;

public class MapResumeStorage extends AbstractStorage{
    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected final Object getSearchKey(String uuid) {
        return storage.get(uuid);
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
    protected final void doDelete(Object searchKey) {
        storage.remove(((Resume) searchKey).getUuid());
    }

    @Override
    protected final Resume doGet(Object searchKey) {
        return storage.get(((Resume) searchKey).getUuid());
    }

    @Override
    protected final void doClear() {
        storage.clear();
    }

    @Override
    protected final void doUpdate(Object searchKey, Resume resume) {
        storage.replace(((Resume) searchKey).getUuid(), resume);
    }

    @Override
    protected final boolean isExisting(Object searchKey) {
        return searchKey != null;
    }
}