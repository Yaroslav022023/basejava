package com.basejava.webapp.exceptions;

public class NotExistStorageException extends StorageException{
    public NotExistStorageException(String uuid) {
        super("Resume '" + uuid + "' not exist", uuid);
    }
}