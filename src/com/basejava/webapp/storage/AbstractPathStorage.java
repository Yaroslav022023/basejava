package com.basejava.webapp.storage;

import com.basejava.webapp.exceptions.StorageException;
import com.basejava.webapp.model.Resume;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class AbstractPathStorage extends AbstractStorage<Path>{
    private final Path directory;

    protected AbstractPathStorage(String dir) {
        Objects.requireNonNull(dir, "directory must be not null.");
        directory = Paths.get(dir);

        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException(dir + " - is not directory.");
        } else if (!Files.isReadable(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " - is not readable / writeable directory.");
        }
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return Path.of(String.valueOf(directory), uuid);
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> resumes = new ArrayList<>();

        try (Stream<Path> paths = Files.list(directory)) {
            paths.forEach(path -> resumes.add(doGet(path)));
        } catch (IOException e) {
            throw new StorageException("doCopyAll error.", null);
        }
        return resumes;
    }

    @Override
    protected void doSave(Resume resume, Path path) {
        try {
            Files.createFile(path);
            doWrite(resume, FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE));
        } catch (IOException e) {
            throw new StorageException("Path save error.", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Path delete error.", path.getFileName().toString(), e);
        }
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return doRead(FileChannel.open(path, StandardOpenOption.READ));
        } catch (IOException e) {
            throw new StorageException("Path get error.", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doClear() {
        try (Stream<Path> path = Files.list(directory)){
            path.forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Path clear error.", null, e);
        }
    }

    @Override
    protected void doUpdate(Path path, Resume resume) {
        try {
            doWrite(resume, FileChannel.open(path, StandardOpenOption.WRITE));
        } catch (IOException e) {
            throw new StorageException("Path update error.", path.getFileName().toString(), e);
        }
    }

    @Override
    protected boolean isExisting(Path path) {
        return Files.exists(path);
    }

    @Override
    public int getSize() {
        try (Stream<Path> paths = Files.list(directory)) {
           return (int) paths.count();
        } catch (IOException e) {
            throw new StorageException("directory read error (empty).", directory.getFileName().toString(), e);
        }
    }

    protected abstract void doWrite(Resume resume, FileChannel os) throws IOException;
    protected abstract Resume doRead(FileChannel is) throws IOException;
}