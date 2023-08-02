package com.basejava.webapp.storage;

import com.basejava.webapp.exceptions.StorageException;
import com.basejava.webapp.model.Resume;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;

public class ObjectStreamPathStorage extends AbstractPathStorage{

    protected ObjectStreamPathStorage(String directory) {
        super(directory);
    }

    @Override
    protected void doWrite(Resume resume, FileChannel os) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(Channels.newOutputStream(os));
        oos.writeObject(resume);
        oos.close();
    }

    @Override
    protected Resume doRead(FileChannel is) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(Channels.newInputStream(is))){
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("resume read error (incorrect type conversion 'Resume').", null, e);
        }
    }
}