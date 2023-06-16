package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

class ArrayStorageTest extends AbstractArrayStorageTest {
    protected ArrayStorageTest() {
        super(new ArrayStorage());
    }

    @Override
    protected Resume[] getAll_CompareWithIndividualStorage() {
        Resume[] resumes = new Resume[5];
        resumes[0] = r1;
        resumes[1] = r2;
        resumes[2] = r3;
        resumes[3] = r4;
        resumes[4] = r5;

        return Arrays.copyOf(resumes, 5);
    }
}