package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

class SortedArrayStorageTest extends AbstractArrayStorageTest{
    protected SortedArrayStorageTest(){
        super(new SortedArrayStorage());
    }

    @Override
    protected Resume[] getAll_CompareWithIndividualStorage() {
        Resume[] resumes = new Resume[5];
        resumes[0] = r5;
        resumes[1] = r1;
        resumes[2] = r2;
        resumes[3] = r4;
        resumes[4] = r3;

        return Arrays.copyOf(resumes, 5);
    }
}