package com.basejava.webapp.util;

import java.io.IOException;

@FunctionalInterface
public interface KeyValueDataStreamReader {
    void readFromDataStream() throws IOException;
}