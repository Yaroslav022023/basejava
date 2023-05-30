package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

public interface Storage {

    Resume[] getAll();
    int getSize();
    void save(Resume resume);
    void delete(String uuid);
    Resume get(String uuid);
    void clear();
    void update(Resume resume);
}