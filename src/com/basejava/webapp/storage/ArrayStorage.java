package com.basejava.webapp.storage;

public class ArrayStorage extends AbstractArrayStorage{

    @Override
    protected int getIndex(String uuid) {
        for (int i = 0; i < countResumes; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }
}