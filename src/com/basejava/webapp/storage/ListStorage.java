package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer>{
    private final List<Resume> storage = new ArrayList<>();

    @Override
    protected final Integer getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return null;
    }

    @Override
    protected final List<Resume> doCopyAll() {
        return new ArrayList<>(storage);
    }

    @Override
    public final int getSize() {
        return storage.size();
    }

    @Override
    protected final void doSave(Resume resume, Integer index) {
        storage.add(resume);
    }

    @Override
    protected final void doDelete(Integer index) {
        storage.remove(index.intValue());
    }

    @Override
    protected final Resume doGet(Integer index) {
        return storage.get(index);
    }

    @Override
    protected final void doClear() {
        storage.clear();
    }

    @Override
    protected final void doUpdate(Integer index, Resume resume) {
        storage.set(index, resume);
    }

    @Override
    protected final boolean isExisting(Integer index) {
        return index != null;
    }
}