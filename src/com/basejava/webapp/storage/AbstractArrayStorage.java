package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

public abstract class AbstractArrayStorage implements Storage{
    protected static final int CAPACITY = 10000;
    protected final Resume[] storage = new Resume[CAPACITY];
    protected int countResumes;

    protected abstract Resume[] getAllConcreteStorage();
    protected abstract void saveConcreteStorage(Resume resume, int index);
    protected abstract void deleteFromConcreteStorage(int index);
    protected abstract Resume getFromConcreteStorage(int index);
    protected abstract void clearConcreteStorage();
    protected abstract void updateConcreteStorage(Resume resume, int index);
    protected abstract int getIndex(String uuid);

    public final Resume[] getAll() {
        return getAllConcreteStorage();
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
            saveConcreteStorage(resume, index);
        }
    }

    public final void delete(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            countResumes--;
            deleteFromConcreteStorage(index);
            System.out.println("The resume '" + uuid + "' was successfully deleted.");
        } else {
            outputMessageNotExistResume(uuid);
        }
    }

    public final Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            return getFromConcreteStorage(index);
        }
        outputMessageNotExistResume(uuid);
        return null;
    }

    public final void clear() {
        clearConcreteStorage();
        countResumes = 0;
    }

    public final void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            updateConcreteStorage(resume, index);
        } else {
            outputMessageNotExistResume(resume.getUuid());
        }
    }

    private void outputMessageNotExistResume(String uuid) {
        System.out.println("Entered resume '" + uuid + "' not exist");
    }
}