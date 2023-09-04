package com.basejava.webapp.storage;

import com.basejava.webapp.util.Config;

public class SqlStorageTest extends AbstractStorageTest {

    protected SqlStorageTest() {
        super(Config.getInstance().getStorage());
    }
}