package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage{
    private final List<Resume> storage = new ArrayList<>();

    @Override
    protected final Object getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return null;
    }

    @Override
    protected final List<Resume> doGetAllSorted() {
        return storage;
    }

    @Override
    public final int getSize() {
        return storage.size();
    }

    @Override
    protected final void doSave(Resume resume, Object index) {
        storage.add(resume);
    }

    @Override
    protected final void doDelete(Object index) {
        storage.remove((int) index);
    }

    @Override
    protected final Resume doGet(Object index) {
        return storage.get((int) index);
    }

    @Override
    protected final void doClear() {
        storage.clear();
    }

    @Override
    protected final void doUpdate(Object index, Resume resume) {
        storage.set((int) index, resume);
    }

    @Override
    protected final boolean isExisting(Object index) {
        return index != null;
    }
}