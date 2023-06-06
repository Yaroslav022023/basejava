package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage{
    protected static final int CAPACITY = 10000;
    protected final Resume[] storage = new Resume[CAPACITY];
    protected int countResumes;

    protected abstract Resume[] doCopyAll();
    protected abstract void insertResume(Resume resume, int index);
    protected abstract void removeResume(int index);
    protected abstract int getIndex(String uuid);

    public final Resume[] getAll() {
        return doCopyAll();
    }

    public final int getSize() {
        return countResumes;
    }

    public final void save(Resume resume) {
        int index = getIndex(resume.getUuid());

        if (countResumes == CAPACITY) {
            System.out.println("The resume storage is full");
        } else if (index >= 0) {
            System.out.println("Such resume '" + resume + "' already added.");
        } else {
            insertResume(resume, index);
            countResumes++;
        }
    }

    public final void delete(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            countResumes--;
            removeResume(index);
            System.out.println("The resume '" + uuid + "' was successfully deleted.");
        } else {
            outputMessageNotExistResume(uuid);
        }
    }

    public final Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            return storage[index];
        }
        outputMessageNotExistResume(uuid);
        return null;
    }

    public final void clear() {
        Arrays.fill(storage, 0, countResumes, null);
        countResumes = 0;
    }

    public final void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            storage[index] = resume;
        } else {
            outputMessageNotExistResume(resume.getUuid());
        }
    }

    private void outputMessageNotExistResume(String uuid) {
        System.out.println("Entered resume '" + uuid + "' not exist");
    }
}