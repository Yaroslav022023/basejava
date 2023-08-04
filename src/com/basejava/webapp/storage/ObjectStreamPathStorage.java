package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.nio.channels.Channels;
import java.nio.channels.FileChannel;

public class ObjectStreamPathStorage extends AbstractPathStorage{
    private SerializationStrategy strategy;

    protected ObjectStreamPathStorage(String directory, SerializationStrategy strategy) {
        super(directory);
        this.strategy = strategy;
    }

    public void setStrategy(SerializationStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    protected void doWrite(Resume resume, FileChannel os) {
        strategy.doWrite(resume, Channels.newOutputStream(os));
    }

    @Override
    protected Resume doRead(FileChannel is) {
        return strategy.doRead(Channels.newInputStream(is));
    }
}