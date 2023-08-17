package com.basejava.webapp.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@FunctionalInterface
public interface KeyValueDataStreamSaver<K, V> {
    void saveToDataStream(Collection<Map.Entry<K, V>> collection, DataOutputStream dos) throws IOException;
}