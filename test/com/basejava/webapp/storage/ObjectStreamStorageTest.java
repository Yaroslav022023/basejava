package com.basejava.webapp.storage;

public class ObjectStreamStorageTest extends AbstractStorageTest{
    protected ObjectStreamStorageTest() {
        super(new ObjectStreamStorage(STORAGE_DIR_FILE));
    }
}