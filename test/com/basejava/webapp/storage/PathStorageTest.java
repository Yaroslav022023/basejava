package com.basejava.webapp.storage;

public class PathStorageTest extends AbstractStorageTest{
    protected PathStorageTest() {
        super(new PathStorage(STORAGE_DIR, new JavaObjectSerialization()));
    }
}