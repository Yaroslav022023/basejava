package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public class ArrayStorage {
    private static final int CAPACITY = 10000;
    Resume[] storage = new Resume[CAPACITY];
    private int countResumes;

    public void clear() {
        Arrays.fill(storage, 0, countResumes, null);
        countResumes = 0;
    }

    public void save(Resume resume) {
        if (countResumes == CAPACITY) {
            System.out.println("The resume storage is full");
            return;
        }

        for (int i = 0; i < countResumes; i++) {
            if (resume.toString().equals(storage[i].getUuid())) {
                System.out.println("The resume '" + resume + "' already added.");
                return;
            }
        }
        storage[countResumes] = resume;
        countResumes++;
    }

    public Resume get(String uuid) {
        for (int i = 0; i < countResumes; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return storage[i];
            }
        }
        return null;
    }

    public void delete(String uuid) {
        for (int i = 0; i < countResumes; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                countResumes--;
                System.arraycopy(storage, i + 1, storage, i, countResumes - i);
                storage[countResumes] = null;
                System.out.println("The resume '" + uuid + "' was successfully deleted.");
                return;
            }
        }
        System.out.println("The entered resume '" + uuid + "' was not found for deletion.");
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, countResumes);
    }

    public int getSize() {
        return countResumes;
    }

    public void update(Resume resume, Resume newResume) {
        for (int i = 0; i < countResumes; i++) {
            if (resume.equals(storage[i])) {
                storage[i].setUuid(newResume.toString());
                return;
            }
        }
        System.out.println("Entered resume: '" + resume + "' was not found for update.");
    }
}