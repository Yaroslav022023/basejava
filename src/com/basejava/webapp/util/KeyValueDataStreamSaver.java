package com.basejava.webapp.util;

import java.io.IOException;

@FunctionalInterface
public interface KeyValueDataStreamSaver<T> {
    void saveToDataStream(T elem) throws IOException;
}