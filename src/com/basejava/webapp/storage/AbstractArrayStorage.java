package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage{
    private static final int CAPACITY = 10000;
    protected final Resume[] storage = new Resume[CAPACITY];
    protected int countResumes;

    public Resume[] getAll() {
        return Arrays.copyOf(storage, countResumes);
    }

    public int getSize() {
        return countResumes;
    }

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

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            countResumes--;
            storage[index] = storage[countResumes];
            storage[countResumes] = null;
            System.out.println("The resume '" + uuid + "' was successfully deleted.");
        } else {
            outputMessageNotExistResume(uuid);
        }
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            return storage[index];
        }
        outputMessageNotExistResume(uuid);
        return null;
    }

    public void clear() {
        Arrays.fill(storage, 0, countResumes, null);
        countResumes = 0;
    }

    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            storage[index] = resume;
        } else {
            outputMessageNotExistResume(resume.getUuid());
        }
    }

    protected abstract int getIndex(String uuid);

    private void outputMessageNotExistResume(String uuid) {
        System.out.println("Entered resume '" + uuid + "' not exist");
    }
}