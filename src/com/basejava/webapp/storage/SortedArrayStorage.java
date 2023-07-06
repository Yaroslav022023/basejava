package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected final Object getSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid, "");
        return Arrays.binarySearch(storage, 0, countResumes, searchKey, comparatorUuid);
    }

    @Override
    protected final void insertResume(Resume resume, int index) {
        int stepIndex = Math.abs(index);
        System.arraycopy(storage, stepIndex - 1, storage, stepIndex, (countResumes - (stepIndex - 1)));
        storage[stepIndex - 1] = resume;
    }

    @Override
    protected final void removeResume(int index) {
        System.arraycopy(storage, index + 1, storage, index, countResumes);
        storage[countResumes] = null;
    }

    private final Comparator<Resume> comparatorUuid = Comparator.comparing(Resume::getUuid);
}