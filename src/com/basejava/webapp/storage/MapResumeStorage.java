package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Map;

public class MapResumeStorage extends MapUuidStorage {

    @Override
    protected final Object getSearchKey(String fullName) {
        for (Map.Entry<String, Resume> entry : storage.entrySet()) {
            if (entry.getValue().getFullName().equals(fullName)) {
                return entry.getKey();
            }
        }
        return null;
    }
}