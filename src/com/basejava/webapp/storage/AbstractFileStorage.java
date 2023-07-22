package com.basejava.webapp.storage;

import com.basejava.webapp.exceptions.StorageException;
import com.basejava.webapp.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File>{
    private final File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must be not null");

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " - is not directory");
        } else if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() +
                    " - is not readable / writeable directory");
        }
        this.directory = directory;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> resumes = new ArrayList<>();

        if (directory != null) {
            File[] files = directory.listFiles();
            for (File file : files) {
                resumes.add(doRead(file));
            }
        }
        return resumes;
    }

    @Override
    protected void doSave(Resume resume, File file) {
        try {
            file.createNewFile();
            doWrite(resume, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }

    }

    @Override
    protected void doDelete(File file) {
        File[] files = directory.listFiles();
        for (File currentFile : files) {
            if (currentFile == file) {
                currentFile.delete();
            }
        }
    }

    @Override
    protected Resume doGet(File file) {
        return doRead(file);
    }

    @Override
    protected void doClear() {
        if (directory != null) {
            File[] files = directory.listFiles();
            for (File file : files) {
                file.delete();
            }
        }
    }

    @Override
    protected void doUpdate(File file, Resume resume) {
        try {
            doWrite(resume, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected boolean isExisting(File file) {
        return file.exists();
    }

    @Override
    public int getSize() {
        return Integer.getInteger(String.valueOf(directory.length()));
    }

    protected abstract void doWrite(Resume resume, File file) throws IOException;
    protected abstract Resume doRead(File file);
}