package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.io.InputStream;
import java.io.OutputStream;

public interface SerializationStrategy {
    void doWrite(Resume resume, OutputStream os);
    Resume doRead(InputStream is);
}