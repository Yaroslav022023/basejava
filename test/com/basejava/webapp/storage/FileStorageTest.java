package com.basejava.webapp.storage;

public class FileStorageTest extends AbstractStorageTest{
    protected FileStorageTest() {
        super(new FileStorage(STORAGE_DIR_FILE, new JavaObjectSerialization()));
    }
}