package com.basejava.webapp.storage;

import com.basejava.webapp.storage.serializer.ObjectStreamSerializer;

public class ObjectFileStorageTest extends AbstractStorageTest{
    protected ObjectFileStorageTest() {
        super(new FileStorage(STORAGE_DIR_FILE, new ObjectStreamSerializer()));
    }
}