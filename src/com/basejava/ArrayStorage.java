package com.basejava;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.Arrays;

public class ArrayStorage {
    private static final int CAPACITY = 10000;
    private final Resume[] storage = new Resume[CAPACITY];
    private int countResumes;

    public void save(Resume resume) {
        storage[countResumes] = resume;
        countResumes++;
    }

    public Resume get(String resume) {
        for (int i = 0; i < countResumes; i++) {
            if (resume.equals(storage[i].getResume())) {
                return storage[i];
            }
        }
        return null;
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, countResumes);
    }

    public void delete(String resume) {
        for (int i = 0; i < countResumes; i++) {
            if (resume.equals(storage[i].getResume())) {
                countResumes--;
                System.arraycopy(storage, i + 1, storage, i, countResumes - i);
                storage[countResumes] = null;
                System.out.println("The resume was successfully deleted.");
                return;
            }
        }
        System.out.println("The entered resume was not found for deletion.");
    }

    public int getSize() {
        return countResumes;
    }

    public void clear() {
        Arrays.fill(storage, 0, countResumes, null);
        countResumes = 0;
    }
}