package com.basejava.webapp.storage;

import com.basejava.webapp.exceptions.StorageException;
import com.basejava.webapp.model.Resume;

import java.io.*;

public class JavaObjectSerializationPath implements SerializationStrategy{

    @Override
    public void doWrite(Resume resume, OutputStream os) {
        try (ObjectOutputStream oos = new ObjectOutputStream(os)){
            oos.writeObject(resume);
        } catch (IOException e) {
            throw new StorageException("resume write error.", null, e);
        }
    }

    @Override
    public Resume doRead(InputStream is) {
        try (ObjectInputStream ois = new ObjectInputStream(is)){
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("resume read error (incorrect type conversion 'Resume').", null, e);
        } catch (IOException e) {
            throw new StorageException("resume read error.", null, e);
        }
    }
}