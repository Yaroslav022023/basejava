package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

public class ArrayStorage extends AbstractArrayStorage{

    public void save(Resume resume) {
        int index = getIndex(resume.getUuid());

        if (countResumes == CAPACITY) {
            System.out.println("The resume storage is full");
        } else if (index >= 0) {
            System.out.println("Such resume '" + resume + "' already added.");
        } else {
            storage[countResumes] = resume;
            countResumes++;
        }
    }

    @Override
    public int getIndex(String uuid) {
        for (int i = 0; i < countResumes; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }
}