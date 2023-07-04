package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.List;

public interface Storage {

    List<Resume> getAllSorted();
    int getSize();
    void save(Resume resume);
    void delete(String uuid);
    Resume get(String uuid);
    void clear();
    void update(Resume resume);
}