package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class ObjectStreamStorage extends AbstractFileStorage{
    private SerializationStrategy strategy;

    protected ObjectStreamStorage(File directory, SerializationStrategy strategy) {
        super(directory);
        this.strategy = strategy;
    }

    public void setStrategy(SerializationStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    protected void doWrite(Resume resume, OutputStream os) {
        strategy.doWrite(resume, os);
    }

    @Override
    protected Resume doRead(InputStream is) {
        return strategy.doRead(is);
    }
}