package com.basejava.webapp.util;

import java.io.IOException;
import java.util.Collection;

@FunctionalInterface
public interface KeyValueDataStreamSaver<K, V> {
    void saveToDataStream(Collection<?> collection) throws IOException;
}