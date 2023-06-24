package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListStorage extends AbstractStorage{
    List<Resume> arrayList = new ArrayList<>();

    public Resume[] getAllFromIndividualStorage() {
        return arrayList.toArray(new Resume[countResumes]);
    }

    protected boolean saveIndividualStorage(Resume resume) {
        if (arrayList.contains(resume)) {
            return false;
        }
        arrayList.add(resume);
        countResumes++;
        return true;
    }

    public final boolean deleteFromIndividualStorage(String uuid) {
        if (iterator(uuid) != null) {
            arrayList.remove(iterator(uuid));
            return true;
        }
        return false;
    }

    public Resume getFromIndividualStorage(String uuid) {
        return iterator(uuid);
    }

    public void clearFromIndividualStorage() {
        arrayList.clear();
    }

    public boolean updateFromIndividualStorage(Resume resume) {
        int index = arrayList.indexOf(resume);
        if (index >= 0) {
            arrayList.set(index, resume);
            return true;
        }
        return false;
    }

    private Resume iterator(String uuid) {
        Iterator<Resume> iterator = arrayList.listIterator();

        while (iterator.hasNext()) {
            if (iterator.next().getUuid().equals(uuid)) {
                return iterator.next();
            }
        }
        return null;
    }
}