package com.basejava.webapp.storage;

public class ObjectStreamPathStorageTest extends AbstractStorageTest{
    protected ObjectStreamPathStorageTest() {
        super(new ObjectStreamPathStorage(STORAGE_DIR, new JavaObjectSerializationPath()));
    }
}