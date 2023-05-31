package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    public final Resume[] sortedStorage = new Resume[CAPACITY];


    @Override
    public void save(Resume resume) {
        int index = getIndex(resume.getUuid());

        if (countResumes == CAPACITY) {
            System.out.println("The resume storage is full");
        } else if (index >= 0) {
            System.out.println("Such resume '" + resume + "' already added.");
        } else {
            int stepIndex = Math.abs(index);
            System.arraycopy(sortedStorage, stepIndex - 1, sortedStorage, stepIndex, (countResumes - (stepIndex - 1)));
            sortedStorage[Math.abs(index) - 1] = resume;
            countResumes++;
        }
    }

    @Override
    public int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(sortedStorage, 0, countResumes, searchKey);
    }
}