package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ListStorage extends AbstractStorage{
    List<Resume> arrayList = new ArrayList<>();

    public final Resume[] getAll() {
        return arrayList.toArray(new Resume[countResumes]);
    }

    protected final void doSave(Resume resume, Object index) {
        arrayList.add(resume);
    }

    protected final void doDelete(Object searchedKey) {
        arrayList.remove((Resume) searchedKey);
    }

    protected final Resume doGet(Object searchedKey) {
        return arrayList.get(arrayList.indexOf((Resume) searchedKey));
    }

    protected final void doClear() {
        arrayList.clear();
    }

    protected final void doUpdate(Resume resume, Object searchedKey) {
        arrayList.set(arrayList.indexOf((Resume) searchedKey), resume);
    }

    protected final Object getSearchKey(String uuid) {
        return iterator(uuid);
    }

    protected final boolean isExisting(Object searchKey) {
        return searchKey != null;
    }

    private Resume iterator(String uuid) {
        ListIterator<Resume> iterator = arrayList.listIterator();

        while (iterator.hasNext()) {
            if (iterator.next().getUuid().equals(uuid)) {
                return iterator.previous();
            }
        }
        return null;
    }
}