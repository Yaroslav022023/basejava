package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    private final Resume[] sortedStorage = new Resume[CAPACITY];

    @Override
    protected Resume[] getAllConcreteStorage() {
        return Arrays.copyOf(sortedStorage, countResumes);
    }

    @Override
    protected void saveConcreteStorage(Resume resume, int index) {
        int stepIndex = Math.abs(index);
        System.arraycopy(sortedStorage, stepIndex - 1, sortedStorage, stepIndex, (countResumes - (stepIndex - 1)));
        sortedStorage[stepIndex - 1] = resume;
        countResumes++;
    }

    @Override
    protected void deleteFromConcreteStorage(int index) {
        System.arraycopy(sortedStorage, index + 1, sortedStorage, index, countResumes);
        sortedStorage[countResumes] = null;
    }

    @Override
    protected Resume getFromConcreteStorage(int index) {
        return sortedStorage[index];
    }

    @Override
    protected void clearConcreteStorage() {
        Arrays.fill(sortedStorage, 0, countResumes, null);
    }

    @Override
    protected void updateConcreteStorage(Resume resume, int index) {
        sortedStorage[index] = resume;
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(sortedStorage, 0, countResumes, searchKey);
    }
}