package com.basejava.webapp.util;

import java.io.IOException;
import java.util.Collection;

@FunctionalInterface
public interface KeyValueDataStreamSaver<T> {
    void saveToDataStream(Collection<T> collection) throws IOException;
}