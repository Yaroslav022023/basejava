package com.basejava.webapp.util;

import com.basejava.webapp.model.Resume;

import java.io.IOException;

@FunctionalInterface
public interface KeyValueDataStreamReader {
    void readFromDataStream(Resume resume) throws IOException;
}