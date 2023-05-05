package com.basejava;

import java.util.Arrays;

public class ArrayStorage {
    private static final int CAPACITY = 10000;
    private final Resume[] storage = new Resume[CAPACITY];
    private int quantityResumes;

    public void save(String resume) {
        storage[quantityResumes] = new Resume(resume);
        quantityResumes++;
    }

    public Resume get(String resume) {
        for (int i = 0; i < quantityResumes; i++) {
            if (resume.equals(storage[i].getResume())) {
                return storage[i];
            }
        }
        return null;
    }

    public void getAll() {
        for (int i = 0; i < quantityResumes; i++) {
            System.out.print(storage[i] + (i != quantityResumes - 1 ? ", " : "\n"));
        }
    }

    public boolean delete(String resume) {
        for (int i = 0; i < quantityResumes; i++) {
            if (resume.equals(storage[i].getResume())) {
                quantityResumes--;
                System.arraycopy(storage, i + 1, storage, i, quantityResumes - i);
                storage[quantityResumes] = null;
                return true;
            }
        }
        return false;
    }

    public int getSize() {
        return quantityResumes;
    }

    public void clear() {
        Arrays.fill(storage, 0, quantityResumes, null);
        quantityResumes = 0;
    }
}