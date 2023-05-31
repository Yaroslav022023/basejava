package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage{
    protected static final int CAPACITY = 5;
    protected final Resume[] storage = new Resume[CAPACITY];
    public int countResumes;

    public Resume[] getAll() {
        return Arrays.copyOf(storage, countResumes);
    }

    public int getSize() {
        return countResumes;
    }

    public abstract void save(Resume resume);

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

    public abstract int getIndex(String uuid);

    private void outputMessageNotExistResume(String uuid) {
        System.out.println("Entered resume '" + uuid + "' not exist");
    }
}