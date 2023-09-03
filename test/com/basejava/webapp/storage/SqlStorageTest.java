package com.basejava.webapp.storage;

import com.basejava.webapp.util.Config;

public class SqlStorageTest extends AbstractStorageTest{

    protected SqlStorageTest() {
        super(new SqlStorage(Config.getInstance().getUrl(),
                Config.getInstance().getUser(),
                Config.getInstance().getPassword()));
    }
}