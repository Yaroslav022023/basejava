package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public class ArrayStorage {
    private static final int CAPACITY = 10000;
    private static final String NOT_FOUND = " - entered resume was not found";
    private int countResumes;
    private int elementExistingResume;
    Resume[] storage = new Resume[CAPACITY];

    public Resume[] getAll() {
        return Arrays.copyOf(storage, countResumes);
    }

    public int getSize() {
        return countResumes;
    }

    public void save(Resume resume) {
        if (countResumes == CAPACITY) {
            System.out.println("The resume storage is full");
            return;
        }

        if (checkUuid(resume.toString())) {
            System.out.println("Such resume '" + resume + "' already added.");
            return;
        }
        storage[countResumes] = resume;
        countResumes++;
    }

    public void delete(String uuid) {
        if (checkUuid(uuid)) {
            countResumes--;
            System.arraycopy(storage, elementExistingResume + 1, storage,
                    elementExistingResume, countResumes - elementExistingResume);
            storage[countResumes] = null;
            System.out.println("The resume '" + uuid + "' was successfully deleted.");
            return;
        }
        System.out.println(uuid + NOT_FOUND);
    }

    public Resume get(String uuid) {
        if (checkUuid(uuid)) {
            return storage[elementExistingResume];
        }
        System.out.println(uuid + NOT_FOUND);
        return null;
    }

    public void clear() {
        Arrays.fill(storage, 0, countResumes, null);
        countResumes = 0;
    }

    public void update(Resume resume, Resume newResume) {
        if (checkUuid(resume.toString())) {
            storage[elementExistingResume].setUuid(newResume.toString());
            return;
        }
        System.out.println(resume + NOT_FOUND);
    }

    private boolean checkUuid(String uuid) {
        for (int i = 0; i < countResumes; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                elementExistingResume = i;
                return true;
            }
        }
        return false;
    }
}