package com.basejava.webapp.storage;

import com.basejava.webapp.exceptions.StorageException;
import com.basejava.webapp.model.Resume;

import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path>{
    private final Path directory;
    private SerializationStrategy strategy;

    protected PathStorage(String dir, SerializationStrategy strategy) {
        Objects.requireNonNull(dir, "directory must be not null.");
        directory = Paths.get(dir);
        this.strategy = strategy;

        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException(dir + " - is not directory.");
        } else if (!Files.isReadable(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " - is not readable / writeable directory.");
        }
    }

    public void setStrategy(SerializationStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> resumes = new ArrayList<>();
        try (Stream<Path> paths = getPaths("doCopyAll error.")){
            paths.forEach(path -> resumes.add(doGet(path)));
        }
        return resumes;
    }

    @Override
    protected void doSave(Resume resume, Path path) {
        try {
            Files.createFile(path);
            strategy.doWrite(resume, Channels.newOutputStream(FileChannel.open(path,
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE)));
        } catch (IOException e) {
            throw new StorageException("path write error." + path.toAbsolutePath(),
                    path.getFileName().toString(), e);
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
            return strategy.doRead(Channels.newInputStream(FileChannel.open(path, StandardOpenOption.READ)));
        } catch (IOException e) {
            throw new StorageException("Path get error.", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doClear() {
        try (Stream<Path> paths = getPaths("Path clear error.")) {
            paths.forEach(this::doDelete);
        }
    }

    @Override
    protected void doUpdate(Path path, Resume resume) {
        try {
            strategy.doWrite(resume, Channels.newOutputStream(FileChannel.open(path, StandardOpenOption.WRITE)));
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
        try (Stream<Path> paths = getPaths("directory read error (empty).")) {
            return (int) paths.count();
        }
    }

    private Stream<Path> getPaths(String textException) {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException(textException, directory.getFileName().toString(), e);
        }
    }
}