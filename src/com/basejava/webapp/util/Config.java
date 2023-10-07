package com.basejava.webapp.util;

import com.basejava.webapp.storage.SqlStorage;
import com.basejava.webapp.storage.Storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Config INSTANCE = new Config();
    private final Storage storage;
    private final File storageDir;

    private Config() {
        File PROPS = new File(getHomeDir() + "/config/resumes.properties");
        try (InputStream is = new FileInputStream(PROPS)) {
            Properties props = new Properties();
            props.load(is);
            storage = new SqlStorage(props.getProperty("db.url"), props.getProperty("db.user"),
                    props.getProperty("db.password"));
            storageDir = new File(props.getProperty("storage.dir"));
        } catch (IOException e) {
            throw new IllegalStateException("Error reading from " +
                    PROPS.getAbsolutePath() + ": " + e.getMessage(), e);
        }
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    public final Storage getStorage() {
        return storage;
    }

    public final File getStorageDir() {
        return storageDir;
    }

    private static File getHomeDir() {
        String prop = System.getProperty("homeDir");
        File homeDir = new File(prop == null ? "." : prop);
        if (!homeDir.isDirectory()) {
            throw new IllegalStateException(homeDir + " is not directory");
        }
        return homeDir;
    }
}